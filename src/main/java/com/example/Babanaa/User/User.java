package com.example.Babanaa.User;

import com.example.Babanaa.Booking.Booking;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String password;
    private String email;
    private int phoneNumber;
    @Lob // Marks the profilePhoto to be stored as a large object
    @Column(name = "profile_photo", columnDefinition = "LONGBLOB")
    private byte[] profilePhoto;
//    TODO: current user location and home Address(if necessary)

/// One-to-many relationship with Bookings
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    // Constructors


    public User() {
    }

    public User(String username, String password, String email, int phoneNumber, byte[] profilePhoto, List<Booking> bookings) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
        this.bookings = bookings;
    }

    public User(Long userId, String username, String password, String email, int phoneNumber, byte[] profilePhoto, List<Booking> bookings) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePhoto = profilePhoto;
        this.bookings = bookings;
    }
    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
