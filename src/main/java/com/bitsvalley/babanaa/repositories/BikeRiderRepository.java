package com.bitsvalley.babanaa.repositories;

import com.bitsvalley.babanaa.domains.BikeRider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BikeRiderRepository extends JpaRepository<BikeRider, Long > {
    Optional<BikeRider> findByEmail(String email);

    Optional<BikeRider> findByPassword(String password);

    @Query("SELECT r FROM BikeRider r WHERE r.phoneNumber=?1 AND r.password=?2")
    Optional<BikeRider> findByCredentials(String phoneNumber,String password);


    Optional<BikeRider> findByPhoneNumber(String phoneNumber);
}
