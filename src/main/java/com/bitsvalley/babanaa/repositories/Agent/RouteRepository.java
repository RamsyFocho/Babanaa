package com.bitsvalley.babanaa.repositories.Agent;

import com.bitsvalley.babanaa.domains.Agent.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findAllByAgentId(Long agentId);

    List<Route> findAllByAgentIdAndStartTimeBetween(Long agentId, LocalDateTime localDateTime, LocalDateTime localDateTime1);
}
