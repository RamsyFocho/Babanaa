package com.bitsvalley.babanaa.domains.Agent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    @JoinColumn(name = "agentId")
    @JsonIgnore
    private Agent agent;

    @ManyToOne
    @JoinColumn(name = "customerId")
    @JsonIgnore
    private Customer customer;

    private Double amountCollected;
    private Double expectedAmount;
    private String currency;
    private ZonedDateTime date;    // For completed
    private ZonedDateTime dueDate; // For pending
    private String status;         // "completed", "pending"
    private String notes;
    // ...getters, setters, constructors...
}
