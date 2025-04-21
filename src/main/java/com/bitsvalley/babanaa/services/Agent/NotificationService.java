package com.bitsvalley.babanaa.services.Agent;

import com.bitsvalley.babanaa.domains.Agent.Notification;
import com.bitsvalley.babanaa.repositories.Agent.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public void markAsRead(Long id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        if(optionalNotification.isPresent()){
            optionalNotification.get().setRead(true);
            notificationRepository.save(optionalNotification.get());
        }
    }

    public Integer getUnreadCount(Long agentId) {
        List<Notification> notifications = notificationRepository.findByAgentId(agentId);
        int unreadCount = 0;
        for (Notification notification : notifications) {
            if (!notification.isRead()) {
                unreadCount++;
            }
        }
        return unreadCount;
    }

    public List<Notification> getAgentNotifications(Long agentId) {
            return notificationRepository.findByAgentId(agentId);

    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
