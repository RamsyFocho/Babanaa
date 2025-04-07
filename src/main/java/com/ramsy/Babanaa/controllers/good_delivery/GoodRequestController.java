package com.ramsy.Babanaa.controllers.good_delivery;

import com.ramsy.Babanaa.domains.BikeRider;
import com.ramsy.Babanaa.domains.good_delivery.DeliveryRequest;
import com.ramsy.Babanaa.services.BikeRiderService;
import com.ramsy.Babanaa.services.UserService;
import com.ramsy.Babanaa.services.good_delivery.GoodDeliveryServices;
import com.ramsy.Babanaa.services.good_delivery.GoodServices;
import com.ramsy.Babanaa.webdomains.DeliveryStatus;
import com.ramsy.Babanaa.webdomains.DeliveryRequestWD;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
        // Log the incoming request
        System.out.println(request);

        try {
            DeliveryRequest createdRequest = goodDeliveryServices.createDeliveryRequest(request);
            sendNewRequest(createdRequest);
            return ResponseEntity.ok(createdRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
        if(Objects.equals(result, "Updated successfully")){
        BikeRider bikeRider = bikeRiderService.getBikeRiderById(riderId);
            return ResponseEntity.ok(Map.of("status","success", "message","Delivery request status updated", "data",bikeRider));
        }
        else{
            return ResponseEntity.ok(Map.of("status","error", "message",result));
//            return ResponseEntity.noContent().build();
        }
    }

}
