package com.ramsy.Babanaa.services;

import com.ramsy.Babanaa.domains.BikeRider;
import com.ramsy.Babanaa.domains.Booking;
import com.ramsy.Babanaa.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    BookingRepository bookingRepository;

    public List<Booking> getBookings() {
    // retrieve all bookings
        return bookingRepository.findAll();
    }
    public List<Booking> getRecentRideRequest(){
        // retrieve all requests for bookings where the Rider
        // is null and the booking time is less thn 5 mins
        LocalDateTime timeThreshold = LocalDateTime.now().minusMinutes(15);
        return bookingRepository.findAllByRiderIsEmpty(timeThreshold);
    }

//    public List<Booking> getBookingsByRider(Long riderId) {
//    // retrieve all bookings associated with the given riderId
//        return bookingRepository.findByRiderId(riderId);
//    }
    public void newBookingRequest(Booking booking){
//        TODO: not allow the same user to have multiple request at the same time
        bookingRepository.save(booking);
    }

    public void updateBooking(Long bookingId, BikeRider rider) {
        System.out.println("In the booking, the Booking Id is "+bookingId);
        Optional<Booking> bookingById = bookingRepository.findById(bookingId);
        if(bookingById.isPresent()) {
//            get the Booking
            Booking booking = bookingById.get();
//             get the rider from the booking to check if at all a rider has been registered
            BikeRider bRider = booking.getBikeRider();
            if(bRider == null){
                booking.setBikeRider(rider);
                bookingRepository.save(booking);
            }else{
                throw new IllegalStateException("booking has already been accepted");
            }
        }else{
            throw new IllegalStateException("Booking not found");
        }
    }


    public Booking getBooKingById(Long bookingId) {
        Optional<Booking> bookingById = bookingRepository.findById(bookingId);
        if(bookingById.isPresent()){
            return bookingById.get();
        } else{
            throw new IllegalStateException("Booking not found");
        }
    }

    public boolean updateRideStatus(long bookingId, String status) {
        Booking newBooking = getBooKingById(bookingId);
        try{
            newBooking.setStatus(status);
            bookingRepository.save(newBooking);
            return true;            
        }catch(Exception e){
            return false;
        }
        
    }
}
