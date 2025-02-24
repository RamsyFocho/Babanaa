package com.bitsvalley.babanaa.repositories.good_delivery;

import com.bitsvalley.babanaa.domains.User;
import com.bitsvalley.babanaa.domains.good_delivery.DeliveryRequest;
import com.bitsvalley.babanaa.domains.good_delivery.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodDeliveryRepository extends JpaRepository<DeliveryRequest,Long> {
    @Query("SELECT dr FROM DeliveryRequest dr WHERE dr.user=?1 AND dr.goods=?2")
    DeliveryRequest findByUserAndGood(User user, Goods goods);
}
