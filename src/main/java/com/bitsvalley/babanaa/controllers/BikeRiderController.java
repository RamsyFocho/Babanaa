package com.bitsvalley.babanaa.controllers;

import com.bitsvalley.babanaa.domains.BikeRider;
import com.bitsvalley.babanaa.webdomains.Location;
import com.bitsvalley.babanaa.services.BikeRiderService;
import com.bitsvalley.babanaa.domains.Booking;
import com.bitsvalley.babanaa.services.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class BikeRiderController {
    Long globalRiderId;

    @Autowired
    BikeRiderService bikeRiderService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
//    ------------------register riders------------------

    @GetMapping("/rider/create")
    public String createRider() {
        return "rider/RiderCredential";
    }
//    ---------------------LOGIN----------------------
    @PostMapping("/rider/login")
    public String LogginRider(@RequestParam("loginEmail") String email,
                             @RequestParam("loginPassword") String password,
                             HttpSession session){
// Store email and password in session
        session.setAttribute("email", email);
        session.setAttribute("password", password);
        return "redirect:/riders";
    }
//    -------------------------REGISTER--------------------------------
    @PostMapping("/rider/register")
    public String registerRider(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("licenseNumber") String licenseNumber,
            @RequestParam("bikeType") String bikeType,
            @RequestParam("bikeName") String bikeName,
            HttpSession session
    ) {
        BikeRider br = new BikeRider();
        br.setName(name);
        br.setEmail(email);
        br.setPassword(password);
        br.setPhoneNumber(phoneNumber);
        br.setLicenseNumber(licenseNumber);
        br.setBikeType(bikeType);
        br.setBikeName(bikeName);

        bikeRiderService.addNewRider(br);
        session.setAttribute("email", email);
        session.setAttribute("password", password);
        return "redirect:/riders";
    }

    @GetMapping("/riders")
    public String getLoggedRiders(HttpSession session) {
        String email= (String) session.getAttribute("email");
        String password= (String) session.getAttribute("password");

        System.out.println("Email: "+email+" Password: "+password);
        if(email==null || password==null){
            return "redirect:/rider/create";
        }
        // Now fetch the user
        BikeRider savedRider = bikeRiderService.getRider(email,password);
        if (savedRider == null) {
            return "error";
        }
        Long Id = savedRider.getRiderId();
//        TODO: Display a welcome message to the user
        globalRiderId=savedRider.getRiderId();
        session.setAttribute("riderId", Id);
        return "redirect:/rider/Dashboard";
    }
    @GetMapping("/rider/Dashboard")
    public String getRiderDashboard(HttpSession session, Model model){
        Long riderId = (Long) session.getAttribute("riderId");
        if(riderId==null || riderId==0){
            return "redirect:/rider/create";
        }else{
            BikeRider rider = bikeRiderService.getBikeRiderById(riderId);
            System.out.println(riderId);
            System.out.println(rider.getName());
            System.out.println(rider.getEmail());
            model.addAttribute("rider",rider);

             return "rider/riderDashboard";
        }
    }
    @PostMapping("/ride/accept")
    @ResponseBody
    public ResponseEntity<?> acceptRideRequest(@RequestBody Long bookingId,HttpSession session) {
        Long riderId = (Long) session.getAttribute("riderId");
        BikeRider rider = bikeRiderService.getBikeRiderById(riderId);
//       update the booking with the rider's Id
        bookingService.updateBooking(bookingId,rider);
//        session the booking id
        session.setAttribute("bookingId",bookingId);
//        notify the customers
        sendRiderDetailsToCustomer(bookingId);
        return ResponseEntity.ok(Map.of("status","success"));
    }

    private void sendRiderDetailsToCustomer(Long bookingId) {
        Booking booking = bookingService.getBooKingById(bookingId);
        BikeRider rider = bikeRiderService.getBikeRiderById(booking.getBikeRider().getRiderId());
        messagingTemplate.convertAndSend("/all/riderAccepted/"+booking.getBookingId(),rider);
    }
//    --------------------automatically collect location and send to the customer in real time
    @PostMapping("/ride/location")
    @ResponseBody
    public ResponseEntity<?> updateLocation(@RequestBody Location location,HttpSession session){
        Long bookingId = (Long) session.getAttribute("bookingId");
        messagingTemplate.convertAndSend("/all/location/"+bookingId,location);
        return  ResponseEntity.ok("Location updated");
    }

}
