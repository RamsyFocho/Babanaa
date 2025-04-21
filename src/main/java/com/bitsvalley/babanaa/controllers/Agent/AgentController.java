package com.bitsvalley.babanaa.controllers.Agent;

import com.bitsvalley.babanaa.domains.Agent.*;
import com.bitsvalley.babanaa.repositories.Agent.AgentRepository;
import com.bitsvalley.babanaa.services.Agent.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private RouteService routeService;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AgentTrackingService agentTrackingService;


    @GetMapping("/profile")
    public ResponseEntity<Agent> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        String phoneNumber = "";
        Optional<Agent> optionalAgent = agentRepository.findByUsername(userDetails.getUsername());
        if(optionalAgent.isPresent()){
            phoneNumber = optionalAgent.get().getPhoneNumber();
        }
        Agent agent = agentService.getAgentByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(agent);
    }

    @GetMapping("/{id}/routes")
    public ResponseEntity<List<Route>> getAgentRoutes(@PathVariable Long agentId) {
        // Implement logic to get all routes for an agent
        return ResponseEntity.ok(routeService.getAgentRoutes(agentId));
    }


    @GetMapping("/{id}/collections")
    public ResponseEntity<List<?>> getAgentCollections(@PathVariable Long agentId) {
        // Implement logic to get all collections for an agent
        // Example response
        return ResponseEntity.ok(collectionService.getAgentCollections(agentId));
    }

    @GetMapping("/{id}/schedules")
    public ResponseEntity<List<?>> getAgentSchedules(@PathVariable Long agentId) {
        // Implement logic to get all schedules for an agent
        List<Schedule> schedules = scheduleService.getAgentSchedules(agentId);
        if (schedules.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(schedules);
    }

@GetMapping("/{id}/notifications")
public ResponseEntity<List<Notification>> getAgentNotifications(@PathVariable Long agentId) {
    // Implement logic to get all notifications for an agent
    List<Notification> notifications = notificationService.getAgentNotifications(agentId);
    if (notifications.isEmpty()) {
        return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(notifications);
}
    @GetMapping("/{id}/tracking-events")
    public ResponseEntity<List<TrackingEvent>> getAgentTrackingEvents(@PathVariable Long agentId) {
        List<TrackingEvent> events = agentTrackingService.getAgentTrackingEvents(agentId);
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }



    @GetMapping("/status")
    public ResponseEntity<?> getStatus(@AuthenticationPrincipal UserDetails userDetails) {
        // Implement logic to get current route status
        // Example response
        return ResponseEntity.ok(agentService.getCurrentStatus(userDetails.getUsername()));
    }

//    @GetMapping("/schedule/today")
//    public ResponseEntity<List<?>> getTodaySchedule(@AuthenticationPrincipal UserDetails userDetails) {
//        // Implement logic to get today's schedule
//        // Example response
//        return ResponseEntity.ok(routeService.getTodaySchedule(userDetails.getUsername()));
//    }


    @PostMapping("/route/start")
    public ResponseEntity<?> startRoute(@AuthenticationPrincipal UserDetails userDetails) {
        // Implement logic to start a route
        // Example response
        return ResponseEntity.ok(agentService.startRoute(userDetails.getUsername()));
    }

    @PostMapping("/route/end")
    public ResponseEntity<?> endRoute(@AuthenticationPrincipal UserDetails userDetails) {
        // Implement logic to end a route
        // Example response
        return ResponseEntity.ok(agentService.endRoute(userDetails.getUsername()));
    }
}