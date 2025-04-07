package com.ramsy.Babanaa.repositories.good_delivery;

import com.ramsy.Babanaa.domains.good_delivery.DeliveryRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodDeliveryRepository extends JpaRepository<DeliveryRequest,Long> {
//    @Query("SELECT d FROM DeliveryRequest d WHERE d.user = :user AND d.good = :good")
//    DeliveryRequest findByUserAndGood(User user, Goods good);

//    DeliveryRequest findByUserAndGoods(User user, List<Goods> storedGoodsList);
}
