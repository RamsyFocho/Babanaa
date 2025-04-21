package com.bitsvalley.babanaa.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        String jwt = null;

        // Try to get JWT from Authorization header
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        }

        // If not in header, try to get from cookies
        if (jwt == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwt_token".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        break;
                    }
                }
            }
        }

        // If no JWT found, continue filter chain
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extract the phone number from the JWT (subject field)
            final String phoneNumber = jwtService.extractUserName(jwt);
            System.out.println("Phone number extracted from JWT: " + phoneNumber);

            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    // Look up the user by phone number using your UserDetailsService
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(phoneNumber);
                    System.out.println("In the filter, the user details is "+userDetails.getUsername());

                    // Validate the token
                    System.out.println("Jwt "+jwt);
                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        System.out.println("Setting Authentication for: " + userDetails.getUsername());

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to find user with phone: " + phoneNumber);
                    System.out.println("Error: " + e.getMessage());
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            System.err.println("Error processing JWT: " + e.getMessage());
            filterChain.doFilter(request, response);
        }
    }
}
