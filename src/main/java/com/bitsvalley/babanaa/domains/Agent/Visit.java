package com.bitsvalley.babanaa.domains.Agent;

import java.time.ZonedDateTime;

public class Visit {
    private String id;
    private Route route;
    private Customer customer;
    private ZonedDateTime visitTime;
    private String status; // "pending", "completed"
    // ...getters, setters, constructors...
}
