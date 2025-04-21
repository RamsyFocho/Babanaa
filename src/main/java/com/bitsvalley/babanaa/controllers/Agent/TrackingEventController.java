package com.bitsvalley.babanaa.controllers.Agent;

import com.bitsvalley.babanaa.domains.Agent.TrackingEvent;
import com.bitsvalley.babanaa.services.Agent.AgentTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api//agent/trackingEvent")
public class TrackingEventController {

    @Autowired
    private AgentTrackingService agentTrackingService;

    // Get all tracking events (filterable)
    @GetMapping
    public ResponseEntity<List<TrackingEvent>> getAllTrackingEvents(@RequestParam(required = false) String filter) {
        List<TrackingEvent> events = agentTrackingService.getAllTrackingEvents(filter);
        return ResponseEntity.ok(events);
    }

    // Log a new tracking event
    @PostMapping
    public ResponseEntity<String> logTrackingEvent(@RequestBody TrackingEvent trackingEvent) {
        String result = agentTrackingService.addTrackingEvent(trackingEvent);
        return ResponseEntity.ok(result);
    }

    // Get tracking event by ID
    @GetMapping("/{id}")
    public ResponseEntity<TrackingEvent> getTrackingEventById(@PathVariable Long id) {
        TrackingEvent event = agentTrackingService.getTrackingEventById(id);
        return ResponseEntity.ok(event);
    }
}

