package com.bitsvalley.babanaa.controllers;

import com.bitsvalley.babanaa.domains.BikeRider;
import com.bitsvalley.babanaa.domains.User;
import com.bitsvalley.babanaa.services.AdminService;
import com.bitsvalley.babanaa.services.BikeRiderService;
import com.bitsvalley.babanaa.services.UserService;
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
