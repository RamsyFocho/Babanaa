package com.ramsy.SanteMboa.controllers.Agent;

import com.ramsy.SanteMboa.domains.Agent.Agent;
import com.ramsy.SanteMboa.services.Agent.AgentService;
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
    public ResponseEntity<?> Login(@RequestBody Agent agentLogin){
        String message = agentService.Login(agentLogin);
        if(Objects.equals(message, "Login Successful")){
            return ResponseEntity.ok(agentService.getAgentByPhoneNumber(agentLogin.getPhoneNumber()));
        }else{
            return ResponseEntity.internalServerError().body(message);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Agent registerAgent){
       String message = agentService.addAgent(registerAgent);
       if(Objects.equals(message, "saved")){
          return ResponseEntity.ok(message);
       }else{
         return ResponseEntity.badRequest().body(message);
       }
    }
}
