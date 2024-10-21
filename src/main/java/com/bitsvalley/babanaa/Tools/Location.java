package com.bitsvalley.babanaa.Tools;

import jakarta.persistence.Embeddable;

@Embeddable
public class Location {
    private String city;
    private String street;
    private double latitude;
    private double longitude;

    public Location() {
    }

    public Location(String city, String street, double latitude, double longitude) {
        this.city = city;
        this.street = street;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

