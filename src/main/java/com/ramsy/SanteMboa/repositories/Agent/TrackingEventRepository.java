package com.ramsy.SanteMboa.repositories.Agent;

import com.ramsy.SanteMboa.domains.Agent.TrackingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface TrackingEventRepository extends JpaRepository<TrackingEvent,Long> {
    List<TrackingEvent> findByAgentUserIdAndEventTimeBetweenOrderByEventTimeAsc(Long agentId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
