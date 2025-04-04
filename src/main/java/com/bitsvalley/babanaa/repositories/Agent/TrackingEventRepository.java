package com.bitsvalley.babanaa.repositories.Agent;

import com.bitsvalley.babanaa.domains.Agent.TrackingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface TrackingEventRepository extends JpaRepository<TrackingEvent,Long> {
    List<TrackingEvent> findByAgentUserIdAndEventTimeBetweenOrderByEventTimeAsc(Long agentId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
