package com.bitsvalley.babanaa.controllers.Agent;

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

//    miscillaneous path
    @GetMapping("/agent/customers")
    public String agentCustomers(){
        return "/Agent/agentCustomer.html";
    }
 @GetMapping("/agent/collection")
    public String agentCollection(){
        return "/Agent/agentCollection.html";
    }
 @GetMapping("/agent/history")
    public String agentHistory(){
        return "/Agent/agentHistory.html";
    }
 @GetMapping("/agent/schedule")
    public String agentSchedule(){
        return "/Agent/agentSchedule.html";
    }
 @GetMapping("/agent/more")
    public String agentMore(){
        return "/Agent/agentMore.html";
    }


}
