package com.bitsvalley.babanaa.domains;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;


@Getter
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "riderId")
    private BikeRider bikeRider;

    private String pickupLocation;


    private String dropoffLocation;
//TODO: Implement location

    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime bookingTime;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime completionTime;

    private float fare;
    private float distance;

    public Booking() {
    }
//----------------------complete booking-----------------------------
    public Booking(User user, BikeRider bikeRider, String pickupLocation, String dropoffLocation, String status, LocalDateTime bookingTime, LocalDateTime completionTime, float fare, float distance) {
        this.user = user;
        this.bikeRider = bikeRider;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.status = status;
        this.bookingTime = bookingTime;
        this.completionTime = completionTime;
        this.fare = fare;
        this.distance = distance;
    }
// -----------------------   for the request booking-----------------------
    public Booking(User user, String pickupLocation, String dropoffLocation, String status, LocalDateTime bookingTime, float fare, float distance) {
        this.user = user;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.status = status;
        this.bookingTime = bookingTime;
        this.fare = fare;
        this.distance = distance;
    }

    public Booking(Long bookingId, User user, BikeRider bikeRider, String pickupLocation, String dropoffLocation, String status, LocalDateTime bookingTime, LocalDateTime completionTime, float fare, float distance) {
        this.bookingId = bookingId;
        this.user = user;
        this.bikeRider = bikeRider;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.status = status;
        this.bookingTime = bookingTime;
        this.completionTime = completionTime;
        this.fare = fare;
        this.distance = distance;
    }
    // Getters and Setters

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBikeRider(BikeRider bikeRider) {
        this.bikeRider = bikeRider;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public void setCompletionTime(LocalDateTime completionTime) {
        this.completionTime = completionTime;
    }

    public void setFare(float fare) {
        this.fare = fare;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
