package com.ramsy.SanteMboa.controllers.Agent;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class routes {
    @GetMapping("/agent")
    public String agentPage(){
        return "/Agent/agent.html";
    }
    @GetMapping("/agent/login")
    public String agentLoginPage(){
        return "/Agent/agentLogin.html";
    }
    @GetMapping("/agent/dashboard")
    public String agentDashboardPage(){
        return "/Agent/agentDashboard.html";
    }
    @GetMapping("/agent/activeRoute")
    public String agentActiveRoutePage(){
        return "/Agent/agentActiveRoute.html";
    }
    @GetMapping("/agent/visit")
    public String agentVisitPage(){
        return "/Agent/agentVisit.html";
    }
    @GetMapping("/agent/routeSummary")
    public String agentRSPage(){
        return "/Agent/agentRouteSummary.html";
    }
    @GetMapping("/agent/settings")
    public String agentSettingsPage(){
        return "/Agent/agentSetting.html";
    }
}
