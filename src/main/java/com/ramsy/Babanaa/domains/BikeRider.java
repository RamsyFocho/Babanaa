package com.ramsy.Babanaa.domains;

import com.ramsy.Babanaa.domains.good_delivery.DeliveryRequest;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Setter
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "riderId"
//)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BikeRider {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long riderId;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String licenseNumber;
    private String bikeType;//scooter, motorcycle etc
    private String bikeColor;
    private String bikeName;//yamaha, honda, sanili etc
    private int bikeYear;
    private String availabilityStatus;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
    private String createdBy;
//    TODO:implement location, ratings of the rider

// One-to-many relationship with Bookings
    @OneToMany(mappedBy = "bikeRider", cascade = CascadeType.ALL)
//    @JsonManagedReference("bookingId")
    private List<Booking> bookings;
    @OneToMany(mappedBy = "bikeRider", cascade = CascadeType.ALL)
//    @JsonManagedReference  // Prevent circular reference when serializing this side
    @JsonIgnore()
    private List<DeliveryRequest> deliveryRequests;

    //constructors

    public BikeRider() {
    }
//    for login
    public BikeRider(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public BikeRider(String name, String email, String password, String phoneNumber, String licenseNumber, String bikeType, String bikeColor, String bikeName) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.bikeType = bikeType;
        this.bikeColor = bikeColor;
        this.bikeName = bikeName;
    }

    public BikeRider(String name, String email, String password, String phoneNumber, String licenseNumber, String bikeType, String bikeColor, String bikeName, int bikeYear, String availabilityStatus, List<Booking> bookings, List<DeliveryRequest> deliveryRequests) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.bikeType = bikeType;
        this.bikeColor = bikeColor;
        this.bikeName = bikeName;
        this.bikeYear = bikeYear;
        this.availabilityStatus = availabilityStatus;
        this.bookings = bookings;
        this.deliveryRequests = deliveryRequests;
    }

    public BikeRider(Long riderId, String name, String email, String password, String phoneNumber, String licenseNumber, String bikeType, String bikeColor, String bikeName, int bikeYear, String availabilityStatus, List<Booking> bookings) {
        this.riderId = riderId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.bikeType = bikeType;
        this.bikeColor = bikeColor;
        this.bikeName = bikeName;
        this.bikeYear = bikeYear;
        this.availabilityStatus = availabilityStatus;
        this.bookings = bookings;
    }
  }
