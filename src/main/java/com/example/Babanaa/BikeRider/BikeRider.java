package com.example.Babanaa.BikeRider;

import com.example.Babanaa.Booking.Booking;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class BikeRider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
//    TODO:implement location, ratings of the rider

// One-to-many relationship with Bookings
    @OneToMany(mappedBy = "bikeRider", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    //constructors

    public BikeRider() {
    }

    public BikeRider(String name, String email, String password, String phoneNumber, String licenseNumber, String bikeType, String bikeColor, String bikeName, int bikeYear, String availabilityStatus, List<Booking> bookings) {
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
    //getters and setters

    public Long getRiderId() {
        return riderId;
    }

    public void setRiderId(Long riderId) {
        this.riderId = riderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getBikeType() {
        return bikeType;
    }

    public void setBikeType(String bikeType) {
        this.bikeType = bikeType;
    }

    public String getBikeColor() {
        return bikeColor;
    }

    public void setBikeColor(String bikeColor) {
        this.bikeColor = bikeColor;
    }

    public String getBikeName() {
        return bikeName;
    }

    public void setBikeName(String bikeName) {
        this.bikeName = bikeName;
    }

    public int getBikeYear() {
        return bikeYear;
    }

    public void setBikeYear(int bikeYear) {
        this.bikeYear = bikeYear;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
