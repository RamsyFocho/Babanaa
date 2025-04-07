package com.ramsy.SanteMboa.services;

import com.ramsy.SanteMboa.domains.User;
import com.ramsy.SanteMboa.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//    -------------returning all the user------------------
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long Id) {
        Optional<User> userById = userRepository.findById(Id);
        if(userById.isPresent()) {
            return userById.get();
        }
        else {
            throw new IllegalStateException("User not found");
        }
    }

    public User getUser(String phoneNumber,String password) {
//        Optional<User> userByEmail = userRepository.findByEmail(phoneNumber);
        System.out.println("In the service\n Phone Number: "+phoneNumber+" Password: "+password);
        Optional<User> userByCredentials = userRepository.findByCredentials(phoneNumber,password);
        return userByCredentials.orElse(null);
    }
    public boolean addNewUser(User user) {
        List<User> userByEmailOrPhoneNumber= userRepository.findByEmailOrPhoneNumber(user.getEmail(), user.getPhoneNumber());
        if(userByEmailOrPhoneNumber!=null) {
            return false;
        }
//        TODO: check if the user is inputing a phoneNumber which is already in the system
        else{
            System.out.println(user);
            userRepository.save(user);
            return true;
        }
    }
    public void removeUser(Long userId) {
        boolean userExists=userRepository.existsById(userId);
        if(!userExists) {
            throw new IllegalStateException("User does not exist");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(Long userId,String userName,String email,String phoneNumber,byte[] photo){
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalStateException("User does not exist"));
        if(userName!=null && !userName.isEmpty() &&
                    !Objects.equals(user.getUsername(), userName)){
            user.setUsername(userName);
        }
        if(email!=null && !email.isEmpty() &&
                !Objects.equals(user.getEmail(), email)){
            user.setEmail(email);
        }
        if(phoneNumber!=null && !phoneNumber.isEmpty() &&
                !Objects.equals(user.getPhoneNumber(), phoneNumber)){
            user.setPhoneNumber(phoneNumber);
        }
        if(photo!=null && !Objects.equals(user.getProfilePhoto(), photo)){
            user.setProfilePhoto(photo);
            user.setLastUpdated();
            user.setCreatedBy(userName);
        }

    }


    public void flush() {
        userRepository.flush();
    }


}
