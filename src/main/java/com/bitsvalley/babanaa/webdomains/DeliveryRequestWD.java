package com.bitsvalley.babanaa.webdomains;

import com.bitsvalley.babanaa.domains.User;
import com.bitsvalley.babanaa.domains.good_delivery.Goods;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class DeliveryRequestWD {
    Long userId;
    List<Goods> goods;
    String pickupLocation;
    String dropoffLocation;
    float fare;

    public DeliveryRequestWD(Long userId, List<Goods> goods, String pickupLocation, String dropoffLocation, float fare) {
        this.userId = userId;
        this.goods = goods;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.fare = fare;
    }
}
