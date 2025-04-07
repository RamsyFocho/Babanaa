//package com.ramsy.SanteMboa.security.jwt;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//public class WebSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.csrf().disable() // Disable CSRF for APIs
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/customer/login").permitAll() // Allow login endpoint
//                        .anyRequest().authenticated() // Protect other routes
//                )
//                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class) // Add JWT Filter
//                .build();
//    }
//}
//
