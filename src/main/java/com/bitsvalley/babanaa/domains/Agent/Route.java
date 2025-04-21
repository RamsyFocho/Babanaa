package com.bitsvalley.babanaa.domains.Agent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Entity
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    @JoinColumn(name = "agentId")
    @JsonIgnore
    private Agent agent;

    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
//    @JsonIgnore
    private List<RouteStop> stops;
    private String status; // "active", "completed"
    // ...getters, setters, constructors...
}