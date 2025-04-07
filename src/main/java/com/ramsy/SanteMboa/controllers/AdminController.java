package com.ramsy.SanteMboa.controllers;

import com.ramsy.SanteMboa.domains.BikeRider;
import com.ramsy.SanteMboa.domains.User;
import com.ramsy.SanteMboa.services.AdminService;
import com.ramsy.SanteMboa.services.BikeRiderService;
import com.ramsy.SanteMboa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private BikeRiderService riderService;

//-----------------------    view everyone-------------------------------------------------------
    @GetMapping("/adminDashboard")
    public String getAllUsers(Model model){
//-----------------------    view all users-------------------------------------------------------

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

//    ---------------------view all riders-----------------------------
        List<BikeRider> riders = riderService.getAllRiders();
        model.addAttribute("riders", riders);
        return "admin/AdminDashboard";
    }




}
