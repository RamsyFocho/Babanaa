package com.bitsvalley.babanaa.controllers.good_delivery;

import com.bitsvalley.babanaa.domains.Booking;
import com.bitsvalley.babanaa.domains.good_delivery.DeliveryRequest;
import com.bitsvalley.babanaa.domains.good_delivery.Goods;
import com.bitsvalley.babanaa.services.good_delivery.GoodDeliveryServices;
import com.bitsvalley.babanaa.services.good_delivery.GoodServices;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    // Implement methods for handling good delivery requests
    // ...
    @PostMapping("/delivery/request")
    public ResponseEntity<?> createGoodDeliveryRequest(@RequestBody DeliveryRequest request) {
        // Implement request creation logic here
        System.out.println(request);
//        get the goods, save it be4 saving the request as a whole
        Goods goods = request.getGoods();
        boolean added = goodServices.addGood(goods);

        if(added){
//            retrieve the stored good
            Goods storedGood = goodServices.getGood(goods.getGoodName(),goods.getDescription());
            if(storedGood != null){
                request.setGoods(storedGood);
            }else {
                return ResponseEntity.ok(Map.of("status","failed","message","The good is null"));

            }
        }
        boolean done = goodDeliveryServices.newDeliveryRequest(request);
        if(done){
        //    get the saved request by Id
            DeliveryRequest savedRequest = goodDeliveryServices.getDeliveryRequest(request.getUser(),request.getGoods());
            if(savedRequest != null){
                return ResponseEntity.ok(Map.of("status","success","message","successfuly added a new request","data",savedRequest));
//            sendNewRequest(savedRequest);
            }else{
                return ResponseEntity.ok(Map.of("status","failed","message","Null request"));

            }
        }
        return ResponseEntity.ok(Map.of("status","failed","message","An error occured"));
    }
    public void sendNewRequest(DeliveryRequest request) {
//        retrieve the requests from the database
        System.out.println("Broadcasting request data: " + request);
        messagingTemplate.convertAndSend("/all/bookings", request);  // Send directly to topic
    }
//    collect both the requestId and the riderId
    @PutMapping("/delivery/accept")
    public ResponseEntity<?> acceptDeliveryRequest(@RequestBody Map<String, Long> jsonDeliveryId) {
        System.out.println("rider wants to accept good request in the acceptDeliveryRequest...");
        return null;
    }

}
