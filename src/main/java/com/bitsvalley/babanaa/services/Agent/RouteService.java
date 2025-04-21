package com.bitsvalley.babanaa.services.Agent;

import com.bitsvalley.babanaa.domains.Agent.Route;
import com.bitsvalley.babanaa.repositories.Agent.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

//    public List<?> getTodaySchedule(String username) {
//    }

    public List<Route> getAgentRoutes(Long agentId) {
        return routeRepository.findAllByAgentId(agentId);

    }

    public List<Route> getRoutesByDate(Long agentId, LocalDate date) {
        return routeRepository.findAllByAgentIdAndStartTimeBetween(agentId, date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }
    public Route saveRoute(Route route) {
        try {
            return routeRepository.save(route);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving route: " + e.getMessage());
        }
    }

    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }

    public Optional<Route> updateRoute(Long id, Route route) {
        return routeRepository.findById(id).map(existingRoute -> {
            existingRoute.setStartTime(route.getStartTime());
            existingRoute.setEndTime(route.getEndTime());
            existingRoute.setAgent(route.getAgent());
            existingRoute.setStops(route.getStops());
            return routeRepository.save(existingRoute);
        });
    }
}
