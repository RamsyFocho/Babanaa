package com.bitsvalley.babanaa.repositories.Agent;

import com.bitsvalley.babanaa.domains.Agent.Notification;

import java.util.List;

public interface NotificationRepository {
    List<Notification> findByAgentId(Long agentId);
}
