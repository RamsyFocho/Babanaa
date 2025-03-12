package com.bitsvalley.babanaa.services.good_delivery;

import com.bitsvalley.babanaa.domains.good_delivery.Goods;
import com.bitsvalley.babanaa.repositories.good_delivery.GoodRepository;
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
                    return true;
                }else{
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("e: ", e);
            return false;
        }
    }
    public Goods getGood(String goodName, String description) {
        Goods good = goodRepository.findByGoodNameAndDescription(goodName,description);
        return good;
    }

}
