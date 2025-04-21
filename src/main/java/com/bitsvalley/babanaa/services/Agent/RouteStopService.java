package com.bitsvalley.babanaa.services.Agent;

import com.bitsvalley.babanaa.domains.Agent.RouteStop;
import com.bitsvalley.babanaa.repositories.Agent.RouteStopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteStopService {
    @Autowired
    private RouteStopRepository routeStopRepository;
    public List<RouteStop> getRouteStops(Long id) {
        return routeStopRepository.getRouteStops(id);
    }

    public RouteStop addRouteStop(Long id, RouteStop routeStop) {
        return routeStopRepository.findById(id)
                .map(existingStop -> {
                    existingStop.setTimestamp(routeStop.getTimestamp());
                    existingStop.setLocation(routeStop.getLocation());
                    existingStop.setAddress(routeStop.getAddress());
                    return routeStopRepository.save(existingStop);
                })
                .orElseThrow(() -> new IllegalArgumentException("Route stop not found"));
    }
}
