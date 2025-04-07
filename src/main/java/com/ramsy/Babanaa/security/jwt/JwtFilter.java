//package com.ramsy.SanteMboa.security.jwt;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.springframework.web.filter.OncePerRequestFilter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class JwtFilter extends OncePerRequestFilter {
//
//    private static final String SECRET_KEY = "your_secret_key"; // Must match the key used in JwtUtil
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//
//        // 1️⃣ Get the Authorization header
//        String authHeader = request.getHeader("Authorization");
//
//        // 2️⃣ If no token or invalid format, continue without authentication
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // 3️⃣ Extract token (remove "Bearer " from header)
//        String token = authHeader.substring(7);
//
//        try {
//            // 4️⃣ Decode the JWT to extract claims
//            Claims claims = Jwts.parser()
//                    .setSigningKey(SECRET_KEY)
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            // 5️⃣ Attach the extracted details (e.g., phoneNumber) to the request
//            request.setAttribute("phoneNumber", claims.getSubject());
//
//        } catch (Exception e) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
//            response.getWriter().write("Invalid Token");
//            return;
//        }
//
//        // 6️⃣ Allow the request to continue to the controller
//        chain.doFilter(request, response);
//    }
//}
//
