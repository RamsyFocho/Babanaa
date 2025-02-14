package com.bitsvalley.babanaa.controllers;

import com.bitsvalley.babanaa.services.UserService;
import com.bitsvalley.babanaa.domains.User;
import com.bitsvalley.babanaa.webdomains.BikeRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/babanaa")
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
    public ResponseEntity<?> LogginUser(@RequestBody User userLogin,
                                     HttpSession session){
        System.out.println(userLogin);
        String phoneNumber = userLogin.getPhoneNumber();
        String password = userLogin.getPassword();
        if(Objects.equals(phoneNumber,"") || Objects.equals(password,"")){
            return ResponseEntity.badRequest().body(Map.of("status", "failed", "message", "Credentials are null"));
        }
        // Store phoneNumber and password in session to retrieve it in the /customers code
        session.setAttribute("phoneNumber", phoneNumber);
        session.setAttribute("password", password);
        // Redirect to /customers endpoint
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/babanaa/customers")
                .build();
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
    public ResponseEntity<?> getLoggedUser(HttpSession session){
        System.out.println("in the /customers api code");
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        String password = (String) session.getAttribute("password");

        System.out.println("phone number: "+phoneNumber+" Password: "+password);

        // Now fetch the user
        User savedUser = userService.getUser(phoneNumber,password);
        if (savedUser == null) {
            return ResponseEntity.ok(Map.of("status","failed","message","user doesn't exist in the system"));
        }
        Long Id = savedUser.getUserId();
//        TODO: Display a welcome message to the user

//        save the Id in the global variable
        globalCusId=savedUser.getUserId();

        session.setAttribute("CusId", Id);
        return ResponseEntity.ok(Map.of("status","success","message","successfully logged in"));

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
        System.out.println("In the booking page, the Customer is "+userId);
        if(userId == null ||userId==0){
            return "redirect:/customer/create";
        }else{
            model.addAttribute("bikeRequest", new BikeRequest());
            session.setAttribute("CusId",userId);
            return "/booking/Booking";
        }
    }


}
