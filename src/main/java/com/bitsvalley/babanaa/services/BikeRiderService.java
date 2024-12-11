package com.bitsvalley.babanaa.services;

import com.bitsvalley.babanaa.domains.BikeRider;
import com.bitsvalley.babanaa.domains.User;
import com.bitsvalley.babanaa.repositories.BikeRiderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class BikeRiderService {
    @Autowired
    BikeRiderRepository bikeRiderRepository;

    public List<BikeRider> getAllRiders() {
        return  bikeRiderRepository.findAll();
    }
    public BikeRider getBikeRiderById(Long Id) {
        Optional<BikeRider> bikeRiderOptional=bikeRiderRepository.findById(Id);
        if(bikeRiderOptional.isPresent()){
            return bikeRiderOptional.get();
        }else{
            throw new IllegalStateException("Rider not found");
        }
    }
//    ------------------Login and registration--------------
    public BikeRider getRider(String email, String password) {
        System.out.println("In the service\n Email: "+email+" Password: "+password);
        Optional<BikeRider> riderByCredentials = bikeRiderRepository.findByCredentials(email,password);
        if(riderByCredentials.isPresent()) {
            return riderByCredentials.get();
        }
        else {
            throw new IllegalStateException("User not found");
        }
    }
    public void addNewRider(BikeRider rider) {
        Optional<BikeRider> riderByEmail= bikeRiderRepository.findByEmail(rider.getEmail());
        if(riderByEmail.isPresent()) {
            throw new IllegalStateException("User already exists");
        }
//        TODO: check if the rider is inputing a phoneNumber which is already in the system
        else{
            System.out.println(rider);
            bikeRiderRepository.save(rider);
        }
    }
}
