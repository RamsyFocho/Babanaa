package com.ramsy.Babanaa.services.good_delivery;

import com.ramsy.Babanaa.domains.good_delivery.Goods;
import com.ramsy.Babanaa.repositories.good_delivery.GoodRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GoodServices {
    @Autowired
    private GoodRepository goodRepository;

    public Boolean addGood(List<Goods> goods){
        try{
            for (Goods good:goods){
                if(good != null){
                    goodRepository.save(good);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("e: ", e);
            return false;
        }
    }

    public Goods getGoodByNameAndDescription(String goodName, String description) {
        return goodRepository.findByGoodNameAndDescription(goodName, description);
    }
    public Goods getGood(String goodName, String description) {
        Goods good = goodRepository.findByGoodNameAndDescription(goodName,description);
        return good;
    }

}
