package com.bitsvalley.babanaa.domains;

import com.bitsvalley.babanaa.domains.good_delivery.DeliveryRequest;
import com.bitsvalley.babanaa.domains.good_delivery.Goods;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "userId"
//)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long userId;
    @Setter
    @Getter
    private String username;
    @Setter
    @Getter
    private String password;
    @Setter
    @Getter
    private String email;
    @Setter
    @Getter
    private String phoneNumber;
    @Setter
    @Getter
    @Lob // Marks the profilePhoto to be stored as a large object
    @Column(name = "profile_photo", columnDefinition = "LONGBLOB")
    private byte[] profilePhoto;
    private LocalDateTime createdAt;
    @Getter
    private LocalDateTime lastUpdated;
    @Setter
    @Getter
    private String createdBy;
//    TODO: current user location and home Address(if necessary)

/// One-to-many relationship with Bookings
    @Setter
    @Getter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    @JsonManagedReference("bookingId")  // Prevent circular reference when serializing this side
    private List<Booking> bookings;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    @JsonManagedReference("deliveryId")  // Unique name for delivery requests
    private List<DeliveryRequest> deliveryRequests;

    // Constructors


    public User() {
    }
    //    for the login only
//TODO: include the list of delivery request inside the constructor
    public User(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

//    for the register

    public User(String username, String password, String email, String phoneNumber, byte[] profilePhoto) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
    }


    public User(String username, String password, String email, String phoneNumber, byte[] profilePhoto, List<Booking> bookings, List<DeliveryRequest> deliveryRequests) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
        this.bookings = bookings;
        this.deliveryRequests = deliveryRequests;
    }
//----------------without profile photo----------------------------------------
    public User(Long userId, String username, String password, String email, String phoneNumber, List<Booking> bookings, List<DeliveryRequest> deliveryRequests) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.bookings = bookings;
        this.deliveryRequests = deliveryRequests;

    }
//    register without profile photo
    public User(String username, String password, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(Long userId, String username, String password, String email, String phoneNumber, byte[] profilePhoto, List<Booking> bookings) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
        this.bookings = bookings;
    }
    // Getters and Setters

    public LocalDateTime getCreated() {
        return createdAt;
    }

    public void setCreated() {
        this.createdAt = LocalDateTime.now();
    }

    public void setLastUpdated() {
        this.lastUpdated = LocalDateTime.now();
    }

}
