package com.bitsvalley.babanaa.controllers;
import com.bitsvalley.babanaa.domains.Booking;
import com.bitsvalley.babanaa.domains.User;
import com.bitsvalley.babanaa.services.BookingService;
import com.bitsvalley.babanaa.services.UserService;
import com.bitsvalley.babanaa.webdomains.BikeRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import org.springframework.http.HttpStatus; // For status codes like BAD_REQUEST or OK


import java.time.LocalDateTime;
import java.util.List;

@Data
@Controller
@Transactional
public class BookingController {
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/ride/request")
    @ResponseBody
    public ResponseEntity<?> getBikeRequest(@RequestBody BikeRequest bikeRequest,HttpSession session) throws Exception {


        String dropOff = bikeRequest.getDropOff();
        String pickup = bikeRequest.getPickup();
        String bikeType = bikeRequest.getBikeType();
//        float fare = bikeRequest.getFare();
        Long Id = (Long) session.getAttribute("CusId");
        if(Id == null ||Id==0){

        }
        System.out.println("Customer Id is: "+ Id);

        User user = userService.getUserById(Id);
//        TODO: Implement dynamically fare and distance
        Booking booking = new Booking(user,pickup,dropOff,"pending", LocalDateTime.now(),500,2);
//    --------------------------save in the database ----------------------------------
        bookingService.newBookingRequest(booking);
        sendNewRequest(booking);

        return ResponseEntity.ok(Map.of("status","success","bookingId",booking.getBookingId()));
    }


    public void sendNewRequest(Booking booking) {
//        retrieve the bookings from the database
        System.out.println("Broadcasting booking data: " + booking);
        messagingTemplate.convertAndSend("/all/bookings", booking);  // Send directly to topic
    }
}
