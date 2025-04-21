package com.bitsvalley.babanaa.config;

import com.bitsvalley.babanaa.domains.Agent.Agent;
import com.bitsvalley.babanaa.repositories.Agent.AgentRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService{
    @Autowired
    private AgentRepository agentRepository;
    private static final String SECRET_KEY = "VT5ii523NWbKA47gygfh5Oj4OPfuonoD/8cROILd7llclCx/bLv4K0DnGdJ0VCFE\n";
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken(
            Map<String,Object> extractClaims,
            UserDetails userDetails
    ){
        System.out.println("Generating token with subject: " + userDetails.getUsername());
        String phoneNumber = "";
        Optional<Agent> optionalAgent = agentRepository.findByUsername(userDetails.getUsername());
        if(optionalAgent.isPresent()){
            phoneNumber = optionalAgent.get().getPhoneNumber();
        }
        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(phoneNumber)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*48))//48 hrs + 1000 milli sec
                .signWith(getSignInKey(), SignatureAlgorithm.HS384)
                .compact();

    }
    public Boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUserName(token);
        String phoneNumber = "";
        Optional<Agent> optionalAgent = agentRepository.findByUsername(userDetails.getUsername());
        if(optionalAgent.isPresent()){
            phoneNumber = optionalAgent.get().getPhoneNumber();
        }
        return (username.equals(phoneNumber)) && !isTokenExpired(token);

    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }
    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

