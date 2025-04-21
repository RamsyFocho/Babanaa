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
import java.util.ArrayList;
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
    public String addTrackingEvent(TrackingEvent event) {
        String message ="";
        Optional<Agent> agentOptional = agentRepository.findByEmail(event.getAgent().getEmail());
        if (agentOptional.isPresent()){
            System.out.println("Agent is present");
            event.setAgent(agentOptional.get());
            try{
                trackingEventRepository.save(event);
                message = "event saved";
            }catch(Exception e){
                message = "An error occurred trying to add an event";
                System.err.println("Error: "+e);
            }
        } else {
            message = "Agent is not found in the database";
            System.err.println("Agent is absent");
        }
        return message;
    }


    public List<TrackingEvent> getTrackingEventsForDay(Long agentId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = date.atTime(23, 59, 59); // 23:59:59

        return trackingEventRepository.findByAgentUserIdAndEventTimeBetweenOrderByEventTimeAsc(
                agentId, startOfDay, endOfDay
        );
    }

    public List<TrackingEvent> getAgentTrackingEvents(Long agentId) {
        return trackingEventRepository.findByAgentUserId(agentId);
    }

    public List<TrackingEvent> getAllTrackingEvents(String filter) {
        return trackingEventRepository.findAll();
    }

    public TrackingEvent getTrackingEventById(Long id) {
        return trackingEventRepository.findById(id).orElseThrow(() -> new RuntimeException("Tracking event with id "+id+" not found"));
    }
}
