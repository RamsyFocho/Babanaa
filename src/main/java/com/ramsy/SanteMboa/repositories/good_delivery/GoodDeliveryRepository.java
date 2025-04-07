package com.ramsy.SanteMboa.repositories.good_delivery;

import com.ramsy.SanteMboa.domains.User;
import com.ramsy.SanteMboa.domains.good_delivery.DeliveryRequest;
import com.ramsy.SanteMboa.domains.good_delivery.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodDeliveryRepository extends JpaRepository<DeliveryRequest,Long> {
//    @Query("SELECT d FROM DeliveryRequest d WHERE d.user = :user AND d.good = :good")
//    DeliveryRequest findByUserAndGood(User user, Goods good);

//    DeliveryRequest findByUserAndGoods(User user, List<Goods> storedGoodsList);
}
