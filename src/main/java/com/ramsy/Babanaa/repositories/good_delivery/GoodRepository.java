package com.ramsy.Babanaa.repositories.good_delivery;

import com.ramsy.Babanaa.domains.good_delivery.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodRepository extends JpaRepository<Goods,Long> {
    Goods findByGoodNameAndDescription(String goodName, String description);
}
