package com.ramsy.Babanaa.services.Agent;

import com.ramsy.Babanaa.domains.Agent.Agent;
import com.ramsy.Babanaa.repositories.Agent.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AgentService {
    @Autowired
    private AgentRepository agentRepository;

    public String addAgent(Agent agent){
        Optional<Agent> agentOptional = agentRepository.findByEmailOrPhoneNumber(agent.getEmail(),agent.getPhoneNumber());
        try{
            if(agentOptional.isEmpty()){
                agentRepository.save(agent);
                return "saved";
            }
            return "Agent found";
        }catch (Exception e){
            System.err.println("Error: "+e );
             return "An error occurred "+ e;
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

    public String Login(Agent agentLogin) {
        Optional <Agent> agentOptional = agentRepository.findByPhoneNumber(agentLogin.getPhoneNumber());
        if(agentOptional.isPresent()){
            Agent agent = agentOptional.get();
            if(agent.getPassword().equals(agentLogin.getPassword())){
                return "Login Successful";
            }else{
                return "Incorrect password";
            }
        }else {
            return "Agent not found";
        }
    }
}
