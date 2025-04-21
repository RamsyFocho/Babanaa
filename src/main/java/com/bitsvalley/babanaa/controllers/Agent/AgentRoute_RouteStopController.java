package com.bitsvalley.babanaa.controllers.Agent;

import com.bitsvalley.babanaa.domains.Agent.Route;
import com.bitsvalley.babanaa.domains.Agent.RouteStop;
import com.bitsvalley.babanaa.services.Agent.RouteService;
import com.bitsvalley.babanaa.services.Agent.RouteStopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api//agent/route")
public class AgentRoute_RouteStopController {
    @Autowired
    private RouteService routeService;

    @Autowired
    private RouteStopService routeStopService;



    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes(@RequestParam Long agentId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Route> routes = routeService.getRoutesByDate(agentId, date);
            return ResponseEntity.ok(routes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Route> createRoute(@RequestBody Route route) {
        try {
            Route savedRoute = routeService.saveRoute(route);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRoute);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
        try {
            Optional<Route> route = routeService.getRouteById(id);
            return route.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Route> updateRoute(@PathVariable Long id, @RequestBody Route route) {
        try {
            Optional<Route> updatedRoute = routeService.updateRoute(id, route);
            return updatedRoute.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//  ---------------------  ROute Stop------------------

    @GetMapping("/{id}/stops")
    public ResponseEntity<List<RouteStop>> getRouteStops(@PathVariable Long id) {
        try {
            List<RouteStop> stops = routeStopService.getRouteStops(id);
            return ResponseEntity.ok(stops);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/stops")
    public ResponseEntity<RouteStop> addRouteStop(@PathVariable Long id, @RequestBody RouteStop routeStop) {
        try {
            RouteStop newStop = routeStopService.addRouteStop(id, routeStop);
            return ResponseEntity.status(HttpStatus.CREATED).body(newStop);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
