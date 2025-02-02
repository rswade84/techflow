package org.taskntech.tech_flow.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.taskntech.tech_flow.models.ResponseTimeMetrics;
import org.taskntech.tech_flow.service.TicketService;

import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

 @Autowired
 private TicketService ticketService;


    //View dashboard analytics page
    @GetMapping
    public String displayAnalytics(Model model) throws JsonProcessingException {

            // Get response time metrics
            ResponseTimeMetrics metrics = ticketService.getResponseTimeMetrics();

            // Send metrics to view
            model.addAttribute("responseMetrics", ticketService.getResponseTimeMetrics());

            //Added array list recentActivityLog to view for recent activity
            model.addAttribute("recentActivity", ticketService.recentActivityLog);

            // Fetches ticket counts by priority
            Map<String, Long> ticketCountByPriority = ticketService.getTicketCountByPriority();
            model.addAttribute("ticketCountByPriority", ticketCountByPriority);
            model.addAttribute("highPriorityCount", ticketCountByPriority.getOrDefault("HIGH", 0L));
            model.addAttribute("mediumPriorityCount", ticketCountByPriority.getOrDefault("MEDIUM", 0L));
            model.addAttribute("lowPriorityCount", ticketCountByPriority.getOrDefault("LOW", 0L));

            // Fetches ticket counts for all statuses
            Map<String, Long> ticketStatusCounts = ticketService.getTicketCountsByStatus();

            // Converts map to JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String ticketStatusCountsJson = objectMapper.writeValueAsString(ticketStatusCounts);

            model.addAttribute("ticketStatusCounts", ticketStatusCountsJson);

        return "dashboard";
    }
}
