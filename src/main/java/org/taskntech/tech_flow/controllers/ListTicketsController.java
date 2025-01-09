package org.taskntech.tech_flow.controllers;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.taskntech.tech_flow.models.PriorityValue;
import org.taskntech.tech_flow.models.StatusUpdates;
import org.taskntech.tech_flow.models.Ticket;
import org.taskntech.tech_flow.service.TicketService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/tickets")
public class ListTicketsController {

        @Autowired
        private TicketService ticketService;

        // List all tickets
        @GetMapping
        public String listTickets(Model model) {
                List<Ticket> tickets = ticketService.getAllTickets();
                if (tickets == null) {
                        tickets = new ArrayList<>(); // Handle null by initializing an empty list
                }
                model.addAttribute("tickets", tickets);
                return "tickets/list";
        }


        // Display the form to create a new ticket
        @GetMapping("/create")
        public String displayCreateTicketForm(Model model) {
                model.addAttribute("ticket", new Ticket());
                model.addAttribute("priorityValues", PriorityValue.values());
                model.addAttribute("statusValues", StatusUpdates.values());
                return "tickets/create";
        }

        // Process the ticket creation form
        @PostMapping("/create")
        public String processCreateTicketForm(
                @Valid @ModelAttribute("ticket") Ticket ticket,
                BindingResult bindingResult,
                Model model) {
                if (bindingResult.hasErrors()) {
                        model.addAttribute("priorityValues", PriorityValue.values());
                        model.addAttribute("statusValues", StatusUpdates.values());
                        model.addAttribute("errorMessage", "Please fix the errors in the form");
                        return "tickets/create";
                }

                try {
                        ticketService.createTicket(ticket);
                        return "redirect:/tickets";
                } catch (ValidationException e) {
                        model.addAttribute("priorityValues", PriorityValue.values());
                        model.addAttribute("statusValues", StatusUpdates.values());
                        model.addAttribute("errorMessage", "Error creating ticket: " + e.getMessage());
                        return "tickets/create";
                }
        }

        // Setter for unit testing
        public void setTicketService(TicketService ticketService) {
                this.ticketService = ticketService;
        }
}
