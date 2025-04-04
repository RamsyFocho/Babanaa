package com.bitsvalley.babanaa.controllers.Agent;

import com.bitsvalley.babanaa.domains.Agent.Agent;
import com.bitsvalley.babanaa.domains.Agent.TrackingEvent;
import com.bitsvalley.babanaa.services.Agent.AgentTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/babanaa/agents")
public class AgentTrackingController {
    @Autowired
    private AgentTrackingService agentTrackingService;

    // Endpoint to add a new tracking event
    @PostMapping("/addTracking")
    public ResponseEntity<?> addTrackingEvent(@RequestBody TrackingEvent trackingEvent) {
        try {
            TrackingEvent event = agentTrackingService.addTrackingEvent(trackingEvent);


            return ResponseEntity.ok(event);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    // Endpoint to get an agent's route between two timestamps
    @GetMapping("/{agentId}/route")
    public ResponseEntity<List<TrackingEvent>> getAgentRoutesForDay(
            @PathVariable Long agentId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<TrackingEvent> events = agentTrackingService.getTrackingEventsForDay(agentId, date);

        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(events);
    }

}
