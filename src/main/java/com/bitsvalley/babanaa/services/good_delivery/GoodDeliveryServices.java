package com.bitsvalley.babanaa.services.good_delivery;
import com.bitsvalley.babanaa.domains.BikeRider;
import com.bitsvalley.babanaa.domains.User;
import com.bitsvalley.babanaa.domains.good_delivery.DeliveryRequest;
import com.bitsvalley.babanaa.domains.good_delivery.Goods;
import com.bitsvalley.babanaa.domains.good_delivery.RequestStatus;
import com.bitsvalley.babanaa.repositories.BikeRiderRepository;
import com.bitsvalley.babanaa.repositories.good_delivery.GoodDeliveryRepository;
import com.bitsvalley.babanaa.services.BikeRiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GoodDeliveryServices {
    @Autowired
    private GoodDeliveryRepository goodDeliveryRepository;
    @Autowired
    private BikeRiderService bikeRiderService;

    public boolean newDeliveryRequest(DeliveryRequest deliveryRequest){
        try{
            goodDeliveryRepository.save(deliveryRequest);
            return true;
        }catch(Exception e){
            return  false;
        }

    }

    public DeliveryRequest getDeliveryRequest(User user, List<Goods> goods) {
        try{
            for(Goods good: goods){
                DeliveryRequest request = goodDeliveryRepository.findByUserAndGood(user, good);
                if(request!=null){
                    return request;
                }
            }
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }

    public DeliveryRequest getDeliveryRequestById(Long deliveryId) {
        Optional<DeliveryRequest> deliveryRequestOptional = goodDeliveryRepository.findById(deliveryId);
        return deliveryRequestOptional.orElse(null);
    }

    public String updateStatus(Long riderId, Long deliveryId, RequestStatus requestStatus) {
        try{
            DeliveryRequest dr = getDeliveryRequestById(deliveryId);
            BikeRider bikeRider = bikeRiderService.getBikeRiderById(riderId);

            if(dr!=null || bikeRider !=null){
                assert dr != null;
                if(dr.getStatus() == RequestStatus.Pending || dr.getStatus() != requestStatus){
                    dr.setBikeRider(bikeRider);
                    dr.setBikeRider(bikeRider);
                    dr.setStatus(requestStatus);
                    if(requestStatus == RequestStatus.Completed){
                        dr.setCompletionTime(LocalDateTime.now());
                    }
                    goodDeliveryRepository.save(dr);
                    return "updated";
                }else{
                    System.out.println("the request has already been accepted");
                    return "already updated";

                }
            }else{
                return "empty";
            }

        }catch (Exception e){
            log.error("e: ", e);
            return null;
        }
    }
}
