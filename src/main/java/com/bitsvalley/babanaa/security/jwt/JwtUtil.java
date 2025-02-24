//package com.bitsvalley.babanaa.security.jwt;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import java.util.Date;
//
//public class JwtUtil {
//    private static final String SECRET_KEY = "bitsVall3yProtection";
//
//    public static String generateToken(String phoneNumber) {
//        return Jwts.builder()
//                .setSubject(phoneNumber)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }
//
//    public static String extractPhoneNumber(String token) {
//        return getClaims(token).getSubject();
//    }
//
//    public static boolean validateToken(String token) {
//        return getClaims(token).getExpiration().after(new Date());
//    }
//
//    private static Claims getClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}
