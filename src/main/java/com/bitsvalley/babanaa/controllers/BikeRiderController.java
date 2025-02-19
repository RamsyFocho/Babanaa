package com.bitsvalley.babanaa.controllers;

import com.bitsvalley.babanaa.domains.BikeRider;
import com.bitsvalley.babanaa.domains.User;
import com.bitsvalley.babanaa.webdomains.Location;
import com.bitsvalley.babanaa.services.BikeRiderService;
import com.bitsvalley.babanaa.domains.Booking;
import com.bitsvalley.babanaa.services.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/babanaa")
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
    public ResponseEntity<?> LogginRider(@RequestBody BikeRider riderLogin,
                             HttpSession session){
        System.out.println("Debugging this api login ...");
        String phoneNumber = riderLogin.getPhoneNumber();
        String password = riderLogin.getPassword();

        if(Objects.equals(phoneNumber,"") || Objects.equals(password,"")){
            return ResponseEntity.badRequest().body(Map.of("status", "failed", "message", "Credentials are null"));
        }
// Store email and password in session
        session.setAttribute("phoneNumber", phoneNumber);
        session.setAttribute("password",password);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/babanaa/riders")
                .build();
    }
//    -------------------------REGISTER--------------------------------
    @PostMapping("/rider/register")
    public ResponseEntity<?> registerRider(
            @RequestBody BikeRider rider,
            HttpSession session
    ) {
        BikeRider br = new BikeRider();
        br.setName(rider.getName());
        br.setEmail(rider.getEmail());
        br.setPassword(rider.getPassword());
        br.setPhoneNumber(rider.getPhoneNumber());
        br.setLicenseNumber(rider.getLicenseNumber());
        br.setBikeType(rider.getBikeType());
        br.setBikeName(rider.getBikeName());

        boolean done = bikeRiderService.addNewRider(br);
        if(done){
            session.setAttribute("phoneNumber", rider.getPhoneNumber());
            session.setAttribute("password", rider.getPassword());
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "/babanaa/riders")
                    .build();
        }else{
            return ResponseEntity.ok(Map.of("status","failed","message","rider exists already"));
        }
    }

    @GetMapping("/riders")
    public ResponseEntity<?> getLoggedRiders(HttpSession session) {
        String phoneNumber= (String) session.getAttribute("phoneNumber");
        String password= (String) session.getAttribute("password");

        System.out.println("PhoneNumber: "+phoneNumber+" Password: "+password);

        // Now fetch the user
        BikeRider savedRider = bikeRiderService.getRider(phoneNumber,password);
        if (savedRider == null) {
            return ResponseEntity.ok(Map.of("status",402,"message","rider not found"));

        }
        Long Id = savedRider.getRiderId();
//        TODO: Display a welcome message to the user
        globalRiderId=savedRider.getRiderId();
        session.setAttribute("riderId", Id);
        return ResponseEntity.ok(Map.of("status",200,"message","rider successfully auth","data",savedRider));
//        return "redirect:/rider/Dashboard";
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
    long bookingId;
    @PutMapping("/ride/accept")
//    @ResponseBody
//    @Transactional
    public ResponseEntity<?> acceptRideRequest(@RequestBody Map<String, Long> jsonBookingId,HttpSession session) {
        System.out.println("Rider wants to accept ride in the Accept ride");
        Long riderId = (Long) session.getAttribute("riderId");
        System.out.println("rider id is "+riderId);
        BikeRider rider = bikeRiderService.getBikeRiderById(riderId);
        bookingId = jsonBookingId.get("bookingId");
        System.out.println("Booking id is "+bookingId);

//       update the booking with the rider's Id
        bookingService.updateBooking(bookingId,rider);
//        session the booking id
        session.setAttribute("bookingId",bookingId);
//        notify the customers
        sendRiderDetailsToCustomer(bookingId);
//        update the rideRequest list on the rider's dashboard
        updateAllRideRequest();
        return ResponseEntity.ok(Map.of("status",200,"bookingId",bookingId));
    }

    private void updateAllRideRequest() {
        try {
            messagingTemplate.convertAndSend("/all/riderAccepted/updateList", "update");
        } catch (Exception e) {
            e.printStackTrace();
            // Log the error properly
        }
    }

    private void sendRiderDetailsToCustomer(Long bookingId) {
        Booking booking = bookingService.getBooKingById(bookingId);
//        BikeRider rider = bikeRiderService.getBikeRiderById(booking.getBikeRider().getRiderId());
        BikeRider rider = booking.getBikeRider();
        System.out.println("In the sendRiderDetailsToCustomer method,");
        System.out.println("the riderId is "+booking.getBikeRider().getRiderId());
        System.out.println("the rider email is "+rider.getEmail());
        System.out.println("The booking id is "+bookingId);
        messagingTemplate.convertAndSend("/all/riderAccepted/"+bookingId,rider);
    }
//    --------------------automatically collect location and send to the customer in real time
    @PostMapping("/ride/location")
    @ResponseBody
    public ResponseEntity<?> updateLocation(@RequestBody Location location,HttpSession session){
        Long bookingId = (Long) session.getAttribute("bookingId");

        messagingTemplate.convertAndSend("/all/location/"+bookingId,location);
        return  ResponseEntity.ok("Location updated");
    }

    @PutMapping("/ride/status")
    @ResponseBody
    public ResponseEntity<?> updateStatus(@RequestBody Map<String, String> jsonStatus){
        System.out.println("the status is");
        System.out.println(jsonStatus.get("status"));
        String status = jsonStatus.get("status");
        if(status == null){
            return ResponseEntity.ok(Map.of("status","failed","message","Status is null"));
        }
        boolean done = bookingService.updateRideStatus(bookingId,status);
        if(!done){
            return ResponseEntity.ok(Map.of("status","failed","message","error while updating the status"));
        }
        return ResponseEntity.ok(Map.of("status","success","message","Succeeded in updating the status"));
    }

}
