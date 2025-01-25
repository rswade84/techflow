package org.taskntech.tech_flow.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.taskntech.tech_flow.service.TicketService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

 @Autowired
 private TicketService ticketService;

    @GetMapping
    public String displayAnalytics(Model model){

            // Displaying response time metrics
            model.addAttribute("responseMetrics", ticketService.getResponseTimeMetrics());

            // Displaying recent activity
            model.addAttribute("recentActivity", ticketService.recentActivityLog);

        return "dashboard";
    }
}
