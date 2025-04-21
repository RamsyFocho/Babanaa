package com.bitsvalley.babanaa.services.Agent;

import com.bitsvalley.babanaa.config.AuthenticationResponse;
import com.bitsvalley.babanaa.config.JwtService;
import com.bitsvalley.babanaa.domains.Agent.Agent;
import com.bitsvalley.babanaa.domains.Agent.EventType;
import com.bitsvalley.babanaa.domains.Agent.TrackingEvent;
import com.bitsvalley.babanaa.repositories.Agent.AgentRepository;
import com.bitsvalley.babanaa.repositories.Agent.TrackingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AgentService {
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TrackingEventRepository trackingEventRepository;

    public AuthenticationResponse addAgent(Agent agent){
        Optional<Agent> agentOptional = agentRepository.findByEmailOrPhoneNumber(agent.getEmail(),agent.getPhoneNumber());
        try{
            if(agentOptional.isEmpty()){
                agent.setPassword(passwordEncoder.encode(agent.getPassword()));
                agent.setCreated();
                agent.setLastUpdated();
                agentRepository.save(agent);
                var jwtToken = jwtService.generateToken(agent);
                return AuthenticationResponse.builder()
                        .token(jwtToken)
                        .build();
            }else{
                // You could throw a custom exception, or return a response with an error status/message.
//                throw new IllegalStateException("Agent with this email or phone number is already registered.");
                return AuthenticationResponse.builder().error("Agent is already registered").build();
            }
        }catch (Exception e){
            System.err.println("Error: "+e );
            return AuthenticationResponse.builder().error("Error: "+e).build();

        }
    }

    private Agent getAgentByEmail(String email) {
        Optional<Agent> agentOptional = agentRepository.findByEmail(email);
        return agentOptional.orElse(null);
    }
    public Agent getAgentByPhoneNumber(String pn) {
        Optional<Agent> agentOptional = agentRepository.findByPhoneNumber(pn);
        return agentOptional.orElse(null);
    }

    public AuthenticationResponse Login(Agent agentLogin) {
        try {
            // Add logging to see the phone number format
            System.out.println("Login attempt with phone: " + agentLogin.getPhoneNumber());
            
            // First check if the agent exists
            Optional<Agent> agentOptional = agentRepository.findByPhoneNumber(agentLogin.getPhoneNumber());
            
            if (agentOptional.isEmpty()) {
                System.out.println("No agent found with phone: " + agentLogin.getPhoneNumber());
                return AuthenticationResponse.builder()
                    .error("No agent found with this phone number")
                    .build();
            }

            try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            agentLogin.getPhoneNumber(),
                            agentLogin.getPassword()
                    )
            );
            } catch (Exception authException) {
                return AuthenticationResponse.builder()
                    .error("Invalid credentials")
                    .build();
            }

            var agent = agentOptional.get();
            System.out.println("Generating token for agent: " + agent.getPhoneNumber());
            var jwtToken = jwtService.generateToken(agent);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            return AuthenticationResponse.builder()
                .error("An unexpected error occurred: " + e.getMessage())
                .build();
        }
    }

    public Map<String, Object> getCurrentStatus(String phoneNumber) {
        // Fetch the agent by phone number
        Agent agent = getAgentByPhoneNumber(phoneNumber);
        if (agent == null) {
            throw new UsernameNotFoundException("Agent not found");
        }

        // Query the latest tracking event for the agent
        List<TrackingEvent> events = trackingEventRepository.findByAgentOrderByEventTimeDesc(agent);

        boolean isActive = false;
        LocalDateTime startTime = null;
        int customersVisited = 0;
        double totalCollected = 0.0;

        for (TrackingEvent event : events) {
            if (event.getEventType() == EventType.START) {
                isActive = true;
                startTime = event.getEventTime();
                break;
            }
            if (event.getEventType() == EventType.VISIT) {
                customersVisited++;
                totalCollected += event.getAmount();
            }
        }

        Map<String, Object> status = new HashMap<>();
        status.put("isActive", isActive);
        status.put("startTime", startTime);
        status.put("customersVisited", customersVisited);
        status.put("totalCustomers", events.size()); // Assuming each event is a customer visit
        status.put("totalCollected", totalCollected);

        return status;
    }

    public Map<String, String> startRoute(String phoneNumber) {
        // Implement logic to start a route
        // Example response
        return Map.of("message", "Route started successfully");
    }

    public Map<String, String> endRoute(String phoneNumber) {
        // Implement logic to end a route
        // Example response
        return Map.of("message", "Route ended successfully");
    }


}
