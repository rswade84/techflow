package org.taskntech.tech_flow.controllers;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.taskntech.tech_flow.exceptions.TicketNotFoundException;
import org.taskntech.tech_flow.models.PriorityValue;
import org.taskntech.tech_flow.models.StatusUpdates;
import org.taskntech.tech_flow.models.Ticket;
import org.taskntech.tech_flow.service.TicketService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
                // Creates an empyt ticket object  and adds it to the model
                model.addAttribute("ticket", new Ticket()); // remember, use of new keyword creates new object
                // Gets all the values from PriorityValues (High, MEDIUM, LOW)
                model.addAttribute("priorityValues", PriorityValue.values());
                // Gets all enum values from StatusUpdates (NOT STARTED, IN PROGRESS, etc)
                model.addAttribute("statusValues", StatusUpdates.values());
                // Returns path to Thymeleaf that will display the form
                return "tickets/create";
        }

        // Add delete ticket
        @PostMapping("/delete/{ticketId}")
        public String deleteTicket(@PathVariable Integer ticketId) {
                try {
                        ticketService.deleteTicket(ticketId);
                        return "redirect:/tickets";
                } catch (TicketNotFoundException e) {
                        return "redirects:/tickets";
                }
        }

        // Add update/edit ticket
        @GetMapping("/edit/{ticketId}")
        public String updateTicketDetails(@PathVariable Integer ticketId, Model model) {
                // Using optional for cases of if the ticket does not exist
                Ticket ticket = ticketService.findTicketById(ticketId);

                // Try to find the ticket and store it
                try {
                        if (ticket == null) {
                                throw new TicketNotFoundException("Ticket not found with ID: " + ticketId);
                        }
                        model.addAttribute("ticket, ticket");
                        return "tickets/edit";

                } catch (TicketNotFoundException e) {
                        return "redirects:/tickets";
                }
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
