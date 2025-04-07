package com.ramsy.SanteMboa.webdomains;

import com.ramsy.SanteMboa.domains.User;
import com.ramsy.SanteMboa.domains.good_delivery.Goods;
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
