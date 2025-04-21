package com.bitsvalley.babanaa.domains.Agent;

import com.bitsvalley.babanaa.webdomains.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
public class RouteStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    @JoinColumn(name = "id")
    @JsonIgnore
    private Route route;

    @Embedded
    private Location location;

    private ZonedDateTime timestamp;
    private String address; // Optional
    // ...getters, setters, constructors...
}
