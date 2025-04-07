package com.ramsy.Babanaa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String landingPage(){
        return "index.html";
    }
}
