package com.bitsvalley.babanaa.services;

import com.bitsvalley.babanaa.domains.Booking;
import com.bitsvalley.babanaa.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
//    TODO: implement this feature
    @Autowired
    BookingRepository bookingRepository;

    public List<Booking> getBookings() {
    // retrieve all bookings
        return bookingRepository.findAll();
    }
    public List<Booking> getRideRequest(){
        // retrieve all requests for bookings
        return bookingRepository.findAllByRiderIsEmpty();
    }

//    public List<Booking> getBookingsByRider(Long riderId) {
//    // retrieve all bookings associated with the given riderId
//        return bookingRepository.findByRiderId(riderId);
//    }
    public void newBookingRequest(Booking booking){
//        TODO: not allow the same user to have multiple request at the same time
        bookingRepository.save(booking);
    }

}
