package com.bitsvalley.babanaa.controllers.good_delivery;

import com.bitsvalley.babanaa.domains.BikeRider;
import com.bitsvalley.babanaa.domains.Booking;
import com.bitsvalley.babanaa.domains.User;
import com.bitsvalley.babanaa.domains.good_delivery.DeliveryRequest;
import com.bitsvalley.babanaa.domains.good_delivery.Goods;
import com.bitsvalley.babanaa.domains.good_delivery.RequestStatus;
import com.bitsvalley.babanaa.services.BikeRiderService;
import com.bitsvalley.babanaa.services.UserService;
import com.bitsvalley.babanaa.services.good_delivery.GoodDeliveryServices;
import com.bitsvalley.babanaa.services.good_delivery.GoodServices;
import com.bitsvalley.babanaa.webdomains.AcceptDelivery;
import com.bitsvalley.babanaa.webdomains.DeliveryRequestWD;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
        // Implement request creation logic here
        System.out.println(request);
        User user = userService.getUserById(request.getUserId());
//        get the goods, save it be4 saving the request as a whole
        Goods goods = request.getGoods();
        boolean added = goodServices.addGood(goods);

        DeliveryRequest dr =  new DeliveryRequest();
        if(added){
//            retrieve the stored good
            Goods storedGood = goodServices.getGood(goods.getGoodName(),goods.getDescription());
            if(storedGood != null){
                request.setGoods(storedGood);
                dr.setUser(user);
                dr.setGoods(request.getGoods());
                dr.setPickupLocation(request.getPickupLocation());
                dr.setDropoffLocation(request.getDropoffLocation());
                dr.setFare(request.getFare());
                dr.setStatus(RequestStatus.Pending);
                dr.setRequestTime(LocalDateTime.now());

            }else {
                return ResponseEntity.ok(Map.of("status","failed","message","The good is null"));
            }
        }
//        save the good delivery request
        boolean done = goodDeliveryServices.newDeliveryRequest(dr);
        if(done){
        //    get the saved request by Id
            DeliveryRequest savedRequest = goodDeliveryServices.getDeliveryRequest(dr.getUser(),request.getGoods());
            if(savedRequest != null){
                sendNewRequest(savedRequest);
                return ResponseEntity.ok(Map.of("status","success","message","successfuly added a new request","data",savedRequest));
            }else{
                return ResponseEntity.ok(Map.of("status","failed","message","Null request"));

            }
        }
        return ResponseEntity.ok(Map.of("status","failed","message","An error occurred"));
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
    public ResponseEntity<?> acceptDeliveryRequest(@RequestBody AcceptDelivery acceptDelivery) {
        Long deliveryId = acceptDelivery.getDeliveryId();
        Long riderId = acceptDelivery.getRiderId();
        System.out.println("rider wants to accept good request in the acceptDeliveryRequest...");
        // Implement request acceptance logic here
        String result = goodDeliveryServices.updateStatus(riderId,deliveryId,RequestStatus.Accepted);
        if(Objects.equals(result, "accepted")){
        BikeRider bikeRider = bikeRiderService.getBikeRiderById(riderId);
            return ResponseEntity.ok(Map.of("status","success", "message","Delivery request accepted", "data",bikeRider));
        } else if (Objects.equals(result, "already accepted")) {
            return ResponseEntity.ok(Map.of("status","failed", "message","Delivery request already accepted"));
        }else{
            return ResponseEntity.ok(Map.of("status","failed", "message","An error occurred. Either the request or the rider doesn't exist in the db"));
//            return ResponseEntity.noContent().build();
        }
    }

}
