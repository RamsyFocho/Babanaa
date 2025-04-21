package com.bitsvalley.babanaa.repositories.Agent;

import com.bitsvalley.babanaa.domains.Agent.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByAgentId(Long agentId);
}
