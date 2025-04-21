package com.bitsvalley.babanaa.domains.Agent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agentId")
    @JsonIgnore
    private Agent agent;
    private LocalDate date; // The day this schedule is for

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    private List<ScheduledTask> tasks;
    private String notes;
    // ...getters, setters, constructors...
}
