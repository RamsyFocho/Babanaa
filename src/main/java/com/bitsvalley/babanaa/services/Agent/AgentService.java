package com.bitsvalley.babanaa.services.Agent;

import com.bitsvalley.babanaa.domains.Agent.Agent;
import com.bitsvalley.babanaa.repositories.Agent.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AgentService {
    @Autowired
    private AgentRepository agentRepository;

    public Agent addAgent(Agent agent){
        Optional<Agent> agentOptional = agentRepository.findByEmail(agent.getEmail());
        if(agentOptional.isEmpty()){
            agentRepository.save(agent);
        }
        return getAgentByEmail(agent.getEmail());
    }

    private Agent getAgentByEmail(String email) {
        Optional<Agent> agentOptional = agentRepository.findByEmail(email);
        return agentOptional.orElse(null);
    }
}
