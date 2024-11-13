package com.bitsvalley.babanaa.repositories;

import com.bitsvalley.babanaa.domains.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.bikeRider IS NULL")
    List<Booking> findAllByRiderIsEmpty();
}

//    List<Booking> findByRiderId(Long riderId);
