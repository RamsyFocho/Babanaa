package com.ramsy.SanteMboa.webdomains;

import com.ramsy.SanteMboa.domains.Booking;
import com.ramsy.SanteMboa.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ride")
public class rideRequestsAPI {
    @Autowired
    private BookingService bookingService;
    @GetMapping("/requests")
    public List<Booking> getAllBookingRequests() {
        // Return booking requests as JSON
        return bookingService.getRecentRideRequest();
    }
}
