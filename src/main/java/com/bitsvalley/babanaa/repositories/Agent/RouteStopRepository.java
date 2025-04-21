package com.bitsvalley.babanaa.repositories.Agent;

import com.bitsvalley.babanaa.domains.Agent.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop,Long> {
    List<RouteStop> getRouteStops(Long id);
}
