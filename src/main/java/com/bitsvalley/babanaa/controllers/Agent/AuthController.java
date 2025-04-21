package com.bitsvalley.babanaa.controllers.Agent;

import com.bitsvalley.babanaa.config.AuthenticationResponse;
import com.bitsvalley.babanaa.domains.Agent.Agent;
import com.bitsvalley.babanaa.services.Agent.AgentService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/babanaa/auth/agents")
public class AuthController {
    @Autowired
    private AgentService agentService;

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody Agent agentLogin, HttpServletResponse response){
        System.out.println("Wants to login------------");
        System.out.println(agentLogin.getPhoneNumber());
        AuthenticationResponse authResponse = agentService.Login(agentLogin);
        if (authResponse.getToken() != null) {
            // Set JWT as cookie
            Cookie cookie = new Cookie("jwt_token", authResponse.getToken());
            cookie.setPath("/");        // Global path
            cookie.setMaxAge(86400);    // 24 hours
            cookie.setHttpOnly(true);   // Can't be accessed by JavaScript
            cookie.setSecure(true);     // HTTPS only in production
            response.addCookie(cookie);
        }

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Agent registerAgent){
        return ResponseEntity.ok(agentService.addAgent(registerAgent));
    }
}
