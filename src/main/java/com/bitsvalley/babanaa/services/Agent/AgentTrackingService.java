package com.bitsvalley.babanaa.services.Agent;

import com.bitsvalley.babanaa.domains.Agent.Agent;
import com.bitsvalley.babanaa.domains.Agent.TrackingEvent;
import com.bitsvalley.babanaa.repositories.Agent.AgentRepository;
import com.bitsvalley.babanaa.repositories.Agent.TrackingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgentTrackingService {
    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private TrackingEventRepository trackingEventRepository;

    @Autowired
    private AgentService agentService;

    @Transactional
    public TrackingEvent addTrackingEvent(TrackingEvent event) {
        Optional<Agent> agentOptional = agentRepository.findByEmail(event.getAgent().getEmail());
        if (agentOptional.isPresent()){
            System.out.println("Agent is present");
            event.setAgent(agentOptional.get());
        } else {
            System.out.println("Agent is absent");
            // Use the agent details from the event to create a new agent
            Agent savedAgent = agentService.addAgent(event.getAgent());
            if(savedAgent !=null){
                event.setAgent(savedAgent);
            }else{
                System.err.println("The saved agent is null");
            }
        }
        System.out.println("Saving TrackingEvent");
        return trackingEventRepository.save(event);
    }


    public List<TrackingEvent> getTrackingEventsForDay(Long agentId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.atTime(23, 59, 59); // 23:59:59

        return trackingEventRepository.findByAgentUserIdAndEventTimeBetweenOrderByEventTimeAsc(
                agentId, startOfDay, endOfDay
        );
    }
}
