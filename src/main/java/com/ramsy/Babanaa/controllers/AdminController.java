package com.ramsy.Babanaa.controllers;

import com.ramsy.Babanaa.domains.BikeRider;
import com.ramsy.Babanaa.domains.User;
import com.ramsy.Babanaa.services.AdminService;
import com.ramsy.Babanaa.services.BikeRiderService;
import com.ramsy.Babanaa.services.UserService;
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
