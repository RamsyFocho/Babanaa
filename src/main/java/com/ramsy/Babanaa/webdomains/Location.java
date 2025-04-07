package com.ramsy.Babanaa.webdomains;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Location {
    // Getters and setters
    private double latitude;
    private double longitude;

}

