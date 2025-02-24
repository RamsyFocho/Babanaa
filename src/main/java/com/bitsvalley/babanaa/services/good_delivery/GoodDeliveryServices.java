package com.bitsvalley.babanaa.services.good_delivery;
import com.bitsvalley.babanaa.domains.User;
import com.bitsvalley.babanaa.domains.good_delivery.DeliveryRequest;
import com.bitsvalley.babanaa.domains.good_delivery.Goods;
import com.bitsvalley.babanaa.repositories.good_delivery.GoodDeliveryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class GoodDeliveryServices {
    @Autowired
    private GoodDeliveryRepository goodDeliveryRepository;

    public boolean newDeliveryRequest(DeliveryRequest deliveryRequest){
        try{
            goodDeliveryRepository.save(deliveryRequest);
            return true;
        }catch(Exception e){
            return  false;
        }

    }

    public DeliveryRequest getDeliveryRequest(User user, Goods good) {
        try{
            DeliveryRequest request = goodDeliveryRepository.findByUserAndGood(user, good);
            if(request!=null){
                return request;
            }
        } catch (Exception e) {
            log.error("e: ", e);
        }
        return null;
    }
}
