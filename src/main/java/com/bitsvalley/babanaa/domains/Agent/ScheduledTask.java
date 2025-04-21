package com.bitsvalley.babanaa.domains.Agent;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
public class ScheduledTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private String taskType; // "visit", "collection", etc.
    private LocalTime plannedTime;
    private String status; // "pending", "completed", "skipped"
    private String notes;
    // ...getters, setters, constructors...
}
