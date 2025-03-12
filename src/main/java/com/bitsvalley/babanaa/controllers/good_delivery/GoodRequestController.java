package com.bitsvalley.babanaa.controllers.good_delivery;

import com.bitsvalley.babanaa.domains.BikeRider;
import com.bitsvalley.babanaa.domains.User;
import com.bitsvalley.babanaa.domains.good_delivery.DeliveryRequest;
import com.bitsvalley.babanaa.domains.good_delivery.Goods;
import com.bitsvalley.babanaa.domains.good_delivery.RequestStatus;
import com.bitsvalley.babanaa.services.BikeRiderService;
import com.bitsvalley.babanaa.services.UserService;
import com.bitsvalley.babanaa.services.good_delivery.GoodDeliveryServices;
import com.bitsvalley.babanaa.services.good_delivery.GoodServices;
import com.bitsvalley.babanaa.webdomains.DeliveryStatus;
import com.bitsvalley.babanaa.webdomains.DeliveryRequestWD;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Log4j2
@RestController
@Data
@Transactional
@RequestMapping("/babanaa")
public class GoodRequestController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private GoodDeliveryServices goodDeliveryServices;
    @Autowired
    private GoodServices goodServices;
    @Autowired
    private UserService userService;
    @Autowired
    private BikeRiderService bikeRiderService;
    // Implement methods for handling good delivery requests
    // ...
    @PostMapping("/delivery/request")
    public ResponseEntity<?> createGoodDeliveryRequest(@RequestBody DeliveryRequestWD request) {
        System.out.println(request);

        // Fetch the user
        User user = userService.getUserById(request.getUserId());

        // Get the list of goods
        List<Goods> goods = request.getGoods();
        if (goods == null || goods.isEmpty()) {
            return ResponseEntity.ok(Map.of("status", "failed", "message", "No goods provided in the request"));
        }

        // Save goods before saving the delivery request
        boolean added = goodServices.addGood(goods);
        if (!added) {
            return ResponseEntity.ok(Map.of("status", "failed", "message", "Failed to save goods"));
        }

        // Retrieve stored goods (assuming method retrieves by some identifying fields)
        List<Goods> storedGoods = new ArrayList<>();
        for (Goods g : goods) {
            Goods retrievedGoods = goodServices.getGood(g.getGoodName(), g.getDescription());
            if (retrievedGoods != null) {
                storedGoods.add(retrievedGoods);
            }
        }

        if (storedGoods.isEmpty()) {
            return ResponseEntity.ok(Map.of("status", "failed", "message", "Failed to retrieve stored goods"));
        }

        // Create and populate the delivery request
        DeliveryRequest dr = new DeliveryRequest();
        dr.setUser(user);
        dr.setGoods(storedGoods);
        dr.setPickupLocation(request.getPickupLocation());
        dr.setDropoffLocation(request.getDropoffLocation());
        dr.setFare(request.getFare());
        dr.setStatus(RequestStatus.Pending);
        dr.setRequestTime(LocalDateTime.now());

        // Save the delivery request
        boolean done = goodDeliveryServices.newDeliveryRequest(dr);
        if (!done) {
            return ResponseEntity.ok(Map.of("status", "failed", "message", "An error occurred while saving the delivery request"));
        }

        // Retrieve the saved request (you might need a better way to fetch it)
        DeliveryRequest savedRequest = goodDeliveryServices.getDeliveryRequest(dr.getUser(), storedGoods);
        if (savedRequest == null) {
            return ResponseEntity.ok(Map.of("status", "failed", "message", "Failed to retrieve saved request"));
        }

        // Send the new request
        sendNewRequest(savedRequest);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Successfully added a new request",
                "data", savedRequest
        ));
    }
    public void sendNewRequest(DeliveryRequest request) {
//        retrieve the requests from the database
        try {
            System.out.println("Broadcasting request data: " + request);
            messagingTemplate.convertAndSend("/all/DeliveryRequest", request);
        }catch(Exception e){
            log.error("error: ", e);
        }
    }
//    collect both the requestId and the riderId
    @PutMapping("/delivery/accept")
    public ResponseEntity<?> acceptDeliveryRequest(@RequestBody DeliveryStatus acceptDelivery) {
        Long deliveryId = acceptDelivery.getDeliveryId();
        Long riderId = acceptDelivery.getRiderId();
        System.out.println(acceptDelivery.getStatus());

        System.out.println("rider wants to accept good request in the acceptDeliveryRequest...");
        // Implement request acceptance logic here
        String result = goodDeliveryServices.updateStatus(riderId,deliveryId,acceptDelivery.getStatus());
        if(Objects.equals(result, "updated")){
        BikeRider bikeRider = bikeRiderService.getBikeRiderById(riderId);
            return ResponseEntity.ok(Map.of("status","success", "message","Delivery request status updated", "data",bikeRider));
        } else if (Objects.equals(result, "already updated")) {
            return ResponseEntity.ok(Map.of("status","failed", "message","Delivery request already updated to the updated status"));
        }else{
            return ResponseEntity.ok(Map.of("status","error", "message","An error occurred. Either the request or the rider doesn't exist in the db"));
//            return ResponseEntity.noContent().build();
        }
    }

}
