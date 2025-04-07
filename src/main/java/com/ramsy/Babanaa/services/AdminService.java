package com.ramsy.Babanaa.services;

import com.ramsy.Babanaa.domains.Admin;
import com.ramsy.Babanaa.domains.BikeRider;
import com.ramsy.Babanaa.domains.User;
import com.ramsy.Babanaa.repositories.AdminRepository;
import com.ramsy.Babanaa.repositories.BikeRiderRepository;
import com.ramsy.Babanaa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BikeRiderRepository riderRepository;

    public Admin getAdmin(String email, String password){
        Optional<Admin> admin = adminRepository.findByEmailAndPassword(email, password);
        if(admin.isPresent()){
            return admin.get();
        }else{
            throw new IllegalStateException("Incorrect credentials. Please try again");
        }
    }
//    list of customers
    public List<User> getUsers() {
    return userRepository.findAll();
}

//    list of riders
    public List<BikeRider> getRiders(){
        return riderRepository.findAll();
    }
//TODO: implement the rest.
}
