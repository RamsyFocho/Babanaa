package com.ramsy.SanteMboa.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "bookingId"
//)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//@Transactional
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference("userId")  // Prevent serialization of the user in this object
    private User user;

    @ManyToOne
    @JoinColumn(name = "riderId")
//    @JsonBackReference("riderId")
    private BikeRider bikeRider;

    private String pickupLocation;


    private String dropoffLocation;

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

}
