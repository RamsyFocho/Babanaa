package com.bitsvalley.babanaa.controllers;

import com.bitsvalley.babanaa.services.UserService;
import com.bitsvalley.babanaa.domains.User;
import com.bitsvalley.babanaa.webdomains.BikeRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class UserController {
    @Autowired
    UserService userService;
//    -------------path to the credentials-------------------

    @GetMapping("/customer/create")
    public String createUser() {
        return "customer/credentials";
    }
//    ---------------login--------------------
    @PostMapping("/customer/login")
    public String LogginUser(@RequestParam("loginEmail") String email,
                             @RequestParam("loginPassword") String password,
                             HttpSession session){
// Store email and password in session
        session.setAttribute("email", email);
        session.setAttribute("password", password);
        return "redirect:/customers";
    }
//--------registration--------------
    @PostMapping("/customer/register")
//    @Transactional
    public String registerUser(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("password") String password,
            @RequestParam("profilePhoto") MultipartFile file,
            HttpSession session) {

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setCreated();
        newUser.setLastUpdated();
        newUser.setCreatedBy(username);

        System.out.println("newUser: " + newUser);
        try {
            if (!file.isEmpty()) {
                newUser.setProfilePhoto(file.getBytes()); // Convert MultipartFile to byte[]
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        // Save the user

        userService.addNewUser(newUser);
         session.setAttribute("email", email);
        session.setAttribute("password", password);

//        getLoggedUser(email, password); // This should work now

        return "redirect:/customers";

    }
    Long globalCusId;
//    -------------GETTING LOGGED USER------------------
    @GetMapping("/customers")
    public String getLoggedUser(HttpSession session){
        String email= (String) session.getAttribute("email");
        String password= (String) session.getAttribute("password");

        System.out.println("Email: "+email+" Password: "+password);
        if(email==null || password==null){
            return "redirect:/customer/create";
        }
        // Now fetch the user
        User savedUser = userService.getUser(email,password);
        if (savedUser == null) {
            return "error";
        }
        Long Id = savedUser.getUserId();
//        TODO: Display a welcome message to the user

//        save the Id in the global variable
        globalCusId=savedUser.getUserId();
        session.setAttribute("CusId", Id);
        return "redirect:/customer/Dashboard";
    }
    @GetMapping("/customer/Dashboard")
    public String Dashboard(HttpSession session, Model model) {
        // Retrieve the user by ID
        Long userId = (Long) session.getAttribute("CusId");
        if(userId == null || userId == 0){
            return "redirect:/customer/create";
        }
        else{

            User user = userService.getUserById(userId);
            globalCusId=userId;
            // Add the user to the model so it can be displayed in the Thymeleaf template
            model.addAttribute("user", user);

            // Return the Thymeleaf template for the dashboard
            return "customer/Dashboard";
        }
    }

//    ----------------------update customer----------------------------------------------------------------
    @PostMapping("/customer/update")
    public String getInfo(
                               @RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("phoneNumber") String phoneNumber,
                               @RequestParam("profilePhoto") MultipartFile file,
                               HttpSession session){
        Long Id= globalCusId;
        System.out.println("Customer Id is: "+ Id);
        byte[] image=null;
        try {
            if (!file.isEmpty()) {
                image = file.getBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
//        session.setAttribute("CusId", Id);
        updateUser(username,email,phoneNumber,image,session);
        return "redirect:/customer/Dashboard";
    }

    @PutMapping("/customer/updateCredentials")
    public void updateUser(String username,
                             String email,
                             String phoneNumber,
                             byte[] image,
                             HttpSession session){

        Long Id = (Long) session.getAttribute("CusId");
        System.out.println("In the update, Customer Id is: "+ Id);
        userService.updateUser(Id,username,email,phoneNumber,image);

    }
//   ------------ default booking path--
    @GetMapping("/booking")
    public String book(Model model, HttpSession session){
        Long userId = (Long) session.getAttribute("CusId");
        if(userId == null ||userId==0){
            return "redirect:/customer/create";
        }else{
            model.addAttribute("bikeRequest", new BikeRequest());
            return "/booking/Booking";
        }
    }


}
