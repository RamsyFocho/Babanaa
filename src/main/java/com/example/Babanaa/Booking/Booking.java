package com.example.Babanaa.Booking;

import com.example.Babanaa.BikeRider.BikeRider;
import com.example.Babanaa.User.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

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
    private Date bookingTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date completionTime;

    private float fare;
    private float distance;

    public Booking() {
    }

    public Booking(User user, BikeRider bikeRider, String pickupLocation, String dropoffLocation, String status, Date bookingTime, Date completionTime, float fare, float distance) {
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

    public Booking(Long bookingId, User user, BikeRider bikeRider, String pickupLocation, String dropoffLocation, String status, Date bookingTime, Date completionTime, float fare, float distance) {
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

    public void setBookingTime(Date bookingTime) {
        this.bookingTime = bookingTime;
    }

    public void setCompletionTime(Date completionTime) {
        this.completionTime = completionTime;
    }

    public void setFare(float fare) {
        this.fare = fare;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
