package com.bitsvalley.babanaa.services.Agent;

import com.bitsvalley.babanaa.domains.Agent.Notification;
import com.bitsvalley.babanaa.repositories.Agent.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    public List<Notification> getAgentNotifications(Long agentId) {
            return notificationRepository.findByAgentId(agentId);

    }
}
