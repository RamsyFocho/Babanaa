package com.ramsy.SanteMboa.controllers;

import com.ramsy.SanteMboa.services.UserService;
import com.ramsy.SanteMboa.domains.User;
import com.ramsy.SanteMboa.webdomains.BikeRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
@RestController
@RequestMapping(value = "/babanaa", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
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
        System.out.println("Session ID before setting attributes: " + session.getId());
        System.out.println(userLogin.getPhoneNumber() + " "+userLogin.getPassword());
        if(userLogin == null){
            return ResponseEntity.badRequest().body(Map.of("status", "failed", "message", "Data is null"));
        }

        String phoneNumber = userLogin.getPhoneNumber();
        String password = userLogin.getPassword();
        session.setAttribute("phoneNumber",phoneNumber);
        session.setAttribute("password",password);

        if(Objects.equals(phoneNumber,"") || Objects.equals(password,"")){
            return ResponseEntity.badRequest().body(Map.of("status", "failed", "message", "Credentials are null"));
        }
        User savedUser = userService.getUser(userLogin.getPhoneNumber(),userLogin.getPassword());
        if(savedUser!=null){
            return ResponseEntity.ok(Map.of("status","success","message","successfully Auth","userId", savedUser.getUserId()));
        }else{
            return ResponseEntity.ok(Map.of("status","failed","message","User not found"));
        }
    }
//--------registration--------------
    @PostMapping("/customer/register")
//    @Transactional
    public ResponseEntity<?> registerUser(
            @RequestBody User userRegistration) {

        User newUser = new User();
        newUser.setEmail(userRegistration.getEmail());
        newUser.setUsername(userRegistration.getUsername());
        newUser.setPassword(userRegistration.getPassword());
        newUser.setPhoneNumber(userRegistration.getPhoneNumber());
        newUser.setCreated();
        newUser.setLastUpdated();
        newUser.setCreatedBy(userRegistration.getUsername());

        byte[] file = userRegistration.getProfilePhoto();
        System.out.println("newUser: " + newUser);
        try {
            if (file != null) {
                newUser.setProfilePhoto(file); // Convert MultipartFile to byte[]
            }
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("status","failed","message", "error dealing with the image"));
        }
        // Save the user

        boolean done = userService.addNewUser(newUser);
        if(done){
            User savedUser = userService.getUser(userRegistration.getPhoneNumber(),userRegistration.getPassword());
            if(savedUser!=null){
                return ResponseEntity.ok(Map.of("status","success","message","successfully Auth","data",savedUser.getUserId()));
            }else{
                return ResponseEntity.ok(Map.of("status","failed","message","User not found"));
            }
        }else{
            return ResponseEntity.ok(Map.of("status","failed","message","User exists already"));
        }
    }
    Long globalCusId;
//    -------------GETTING LOGGED USER------------------
    @GetMapping( value= "/loggedCustomers")
    public ResponseEntity<?> getLoggedUser(HttpSession session){

        System.out.println("in the /customers api code...");

        if(session==null){
            return ResponseEntity.ok(Map.of("status","error","message","Session is null"));
        }
        System.out.println("Session ID in /customers: " + session.getId());
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        String password = (String) session.getAttribute("password");

        System.out.println("phone number: "+phoneNumber+" Password: "+password);
        // Now fetch the user
        User savedUser;
        savedUser = userService.getUser(phoneNumber,password);
        if (savedUser == null) {
            return ResponseEntity.ok(Map.of("status","failed","message","User is null or not found"));
        }
        Long Id = savedUser.getUserId();
//        TODO: Display a welcome message to the user

//        save the Id in the global variable
        globalCusId=savedUser.getUserId();

        session.setAttribute("CusId", Id);
        return ResponseEntity.ok(Map.of("status","success","message","successfully Auth","data",savedUser));

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
