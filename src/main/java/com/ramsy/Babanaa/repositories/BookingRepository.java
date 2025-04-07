package com.ramsy.Babanaa.repositories;

import com.ramsy.Babanaa.domains.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.bikeRider IS NULL AND b.bookingTime >= :timeThreshold ORDER BY b.bookingTime ")
    List<Booking> findAllByRiderIsEmpty(@Param("timeThreshold") LocalDateTime timeThreshold);

//    select booking where bookingId is Id and bikeRider is null
//    @Query("SELECT b FROM Booking b WHERE b.bikeRider IS NULL AND b.bookingId = :Id")
//    Optional<Booking> findBookingByIdAndNullRider(@Param("Id") Long bookingId);

}

//    List<Booking> findByRiderId(Long riderId);
