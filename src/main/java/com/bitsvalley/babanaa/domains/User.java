package com.bitsvalley.babanaa.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
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
    @Getter
    private String createdBy;
//    TODO: current user location and home Address(if necessary)

/// One-to-many relationship with Bookings
    @Getter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    // Constructors


    public User() {
    }

    public User(String username, String password, String email, String phoneNumber, byte[] profilePhoto, List<Booking> bookings) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
        this.bookings = bookings;
    }
//----------------without profile photo----------------------------------------
    public User(Long userId, String username, String password, String email, String phoneNumber, List<Booking> bookings) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.bookings = bookings;
    }

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

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
    public LocalDateTime getCreated() {
        return createdAt;
    }

    public void setCreated() {
        this.createdAt = LocalDateTime.now();
    }

    public void setLastUpdated() {
        this.lastUpdated = LocalDateTime.now();
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}
