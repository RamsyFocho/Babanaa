package com.bitsvalley.babanaa.controllers.Agent;

import com.bitsvalley.babanaa.domains.Agent.Notification;
import com.bitsvalley.babanaa.services.Agent.AgentService;
import com.bitsvalley.babanaa.services.Agent.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AgentService agentService;
    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam(name = "agentId", required = false) Long agentId) {
        return ResponseEntity.ok(notificationService.getAgentNotifications(agentId));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Integer> getUnreadCount(@RequestParam(name = "agentId", required = false) Long agentId) {
        return ResponseEntity.ok(notificationService.getUnreadCount(agentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
