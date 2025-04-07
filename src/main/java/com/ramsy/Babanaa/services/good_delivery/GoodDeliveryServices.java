package com.ramsy.Babanaa.services.good_delivery;
import com.ramsy.Babanaa.domains.BikeRider;
import com.ramsy.Babanaa.domains.User;
import com.ramsy.Babanaa.domains.good_delivery.DeliveryRequest;
import com.ramsy.Babanaa.domains.good_delivery.Goods;
import com.ramsy.Babanaa.domains.good_delivery.RequestStatus;
import com.ramsy.Babanaa.repositories.UserRepository;
import com.ramsy.Babanaa.repositories.good_delivery.GoodDeliveryRepository;
import com.ramsy.Babanaa.repositories.good_delivery.GoodRepository;
import com.ramsy.Babanaa.services.BikeRiderService;
import com.ramsy.Babanaa.webdomains.DeliveryRequestWD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class GoodDeliveryServices {
    @Autowired
    private GoodDeliveryRepository goodDeliveryRepository;
    @Autowired
    private BikeRiderService bikeRiderService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GoodRepository goodsRepository;

    @Transactional
    public DeliveryRequest createDeliveryRequest(DeliveryRequestWD requestWD) {
        // Find the user from the database
        User user = userRepository.findById(requestWD.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create new DeliveryRequest
        DeliveryRequest deliveryRequest = new DeliveryRequest();
        deliveryRequest.setUser(user);
        deliveryRequest.setPickupLocation(requestWD.getPickupLocation());
        deliveryRequest.setDropoffLocation(requestWD.getDropoffLocation());
        deliveryRequest.setFare(requestWD.getFare());
        deliveryRequest.setRequestTime(LocalDateTime.now());
        deliveryRequest.setStatus(RequestStatus.Pending);

        // Assign Goods to DeliveryRequest
        List<Goods> goodsList = requestWD.getGoods();
        for (Goods good : goodsList) {
            good.setDeliveryRequest(deliveryRequest);
        }

        // Set goods and save everything in one go
        deliveryRequest.setGoods(goodsList);
        return goodDeliveryRepository.save(deliveryRequest);
    }

//    public DeliveryRequest getDeliveryRequest(User user, List<Goods> goods) {
//        try{
//            for(Goods good: goods){
//                DeliveryRequest request = goodDeliveryRepository.findByUserAndGood(user, good);
//                if(request!=null){
//                    return request;
//                }
//            }
//        } catch (Exception e) {
//            log.error("e: ", e);
//        }
//        return null;
//    }

    public DeliveryRequest getDeliveryRequestById(Long deliveryId) {
        Optional<DeliveryRequest> deliveryRequestOptional = goodDeliveryRepository.findById(deliveryId);
        return deliveryRequestOptional.orElse(null);
    }

    public String updateStatus(Long riderId, Long deliveryId, RequestStatus requestStatus) {
        try {
            // Fetch delivery request and bike rider
            DeliveryRequest dr = getDeliveryRequestById(deliveryId);
            BikeRider bikeRider = bikeRiderService.getBikeRiderById(riderId);

            // Validate existence of delivery request and bike rider
            if (dr == null || bikeRider == null) {
                return "Delivery request or rider not found";
            }

            // Prevent changing status if already completed
            if (dr.getStatus() == RequestStatus.Completed) {
                return "This delivery request is already completed and cannot be updated";
            }

            // Ensure the correct status transition
            if (!isValidStatusTransition(dr.getStatus(), requestStatus)) {
                return "Invalid status transition";
            }

            // Prevent assigning a new rider if one is already assigned
            if (dr.getBikeRider() != null && !dr.getBikeRider().getRiderId().equals(riderId)) {
                return "A different rider is already assigned to this request";
            }

            // Assign rider if not already assigned
            if (dr.getBikeRider() == null) {
                dr.setBikeRider(bikeRider);
            }

            // Update status
            dr.setStatus(requestStatus);

            // Set completion time if status is changed to "Completed"
            if (requestStatus == RequestStatus.Completed) {
                dr.setCompletionTime(LocalDateTime.now());
            }

            // Save updated request
            goodDeliveryRepository.save(dr);
            return "Updated successfully";

        } catch (Exception e) {
            log.error("Error updating delivery status: ", e);
            return "Error processing request";
        }
    }
    /**
     * Validates if a status transition is allowed.
     * Allowed normal flow:
     *   Pending -> Accepted
     *   Accepted -> PickedUp
     *   PickedUp -> InTransit
     *   InTransit -> Delivered
     *   Delivered -> Completed
     *
     * Exceptions:
     *   Pending -> Cancelled
     *   Accepted -> Failed
     *   Delivered -> Refunded
     */
    private boolean isValidStatusTransition(RequestStatus currentStatus, RequestStatus newStatus) {
        Map<RequestStatus, List<RequestStatus>> validTransitions = new HashMap<>();

        // Combine normal flow transitions with exceptions:
        validTransitions.put(RequestStatus.Pending, Arrays.asList(RequestStatus.Accepted, RequestStatus.Cancelled));
        validTransitions.put(RequestStatus.Accepted, Arrays.asList(RequestStatus.PickedUp, RequestStatus.Failed));
        validTransitions.put(RequestStatus.PickedUp, Arrays.asList(RequestStatus.InTransit));
        validTransitions.put(RequestStatus.InTransit, Arrays.asList(RequestStatus.Delivered));
        validTransitions.put(RequestStatus.Delivered, Arrays.asList(RequestStatus.Completed, RequestStatus.Refunded));

        // Check if the new status is allowed from the current status
        return validTransitions.containsKey(currentStatus) && validTransitions.get(currentStatus).contains(newStatus);
    }
}
