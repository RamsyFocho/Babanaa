package com.ramsy.SanteMboa.domains.Agent;

import com.ramsy.SanteMboa.webdomains.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class TrackingEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "agentId")
    @JsonIgnore
    private Agent agent;

    @Embedded
    private Location location;

    private Double amount;

    private LocalDateTime eventTime;

    @Enumerated(EnumType.STRING)
    private EventType eventType;


}

