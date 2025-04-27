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
import org.taskntech.tech_flow.service.PopulateTable;
import org.taskntech.tech_flow.service.TicketService;
import org.taskntech.tech_flow.controllers.NotificationController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller // Indicates a Spring MVC controller
@RequestMapping("/tickets") // Sets the base URL/Endpoint to /tickets
public class ListTicketsController {

        @Autowired // Injects TicketService dependency for database operations
        private TicketService ticketService;

        @Autowired
        private PopulateTable populateTable;

        // List all tickets, GetMapping indicates a GET request
        @GetMapping
        public String listTickets(Model model, @RequestParam(name = "sortBy", required = false, defaultValue = "Default") String sortBy ) {

                //List sorted tickets default by ID
                List<Ticket> tickets = ticketService.getTicketList(sortBy);
                if (tickets == null) {
                        tickets = new ArrayList<>(); // Handle null by initializing an empty list
                }

                // Accepts "Key/Value" pairs. Key = is string identifier (thymeleaf), Value = is the data passed
                model.addAttribute("tickets", tickets);
                model.addAttribute("sortBy", sortBy);
                model.addAttribute("sortOptions",  ticketService.sortOptions);

                return "/tickets/list";
        }

        // Display the form to create a new ticket
        @GetMapping("/create")
        public String displayCreateTicketForm(Model model) {
                // Instantiates a new ticket, via "new" keyword
                model.addAttribute("ticket", new Ticket());

                // Sets values to view from PriorityValue (Model - High, MEDIUM, LOW)n
                model.addAttribute("priorityValues", PriorityValue.values());
                // Sets values to view from StatusUpdates (Model - NOT STARTED, IN PROGRESS, DELAYED, RESOLVED, CLOSED)
                model.addAttribute("statusValues", StatusUpdates.values());

                return "tickets/create";
        }

        // after closed ticket button is pressed
        @PostMapping("/close/{ticketId}")
        public String closeTicket(@PathVariable Integer ticketId) {
                try {
                        //closes ticket and removes from view
                        ticketService.closeTicket(ticketId);
                        return "redirect:/tickets";
                } catch (TicketNotFoundException e) {
                        return "redirect:/tickets";
                }
        }

        // Display edit form for existing ticket
        @GetMapping("/edit/{ticketId}")
        public String showEditForm(@PathVariable Integer ticketId, Model model) {

                Ticket ticket = ticketService.findTicketById(ticketId);

                // Look for ticket that you are trying to edit
                if (ticket == null) {
                        throw new TicketNotFoundException("Ticket not found with ID: " + ticketId);
                }
                model.addAttribute("ticket", ticket); // Add existing ticket from line #80
                model.addAttribute("priorityValues", PriorityValue.values()); // Adds the priority values
                model.addAttribute("statusValues", StatusUpdates.values()); // Adds the status values
                return "tickets/edit";
        }

        // Send POST request to submit data from form
        @PostMapping("/create")
        public String processCreateTicketForm(
                @Valid @ModelAttribute("ticket") Ticket ticket, // ModelAtt binds data to ticket object
                BindingResult bindingResult, // BindingResult stores validation errors, if any
                Model model) { // Model interface is needed to use model.addAttribute for view
                if (bindingResult.hasErrors()) {
                        model.addAttribute("priorityValues", PriorityValue.values());
                        model.addAttribute("statusValues", StatusUpdates.values());
                        model.addAttribute("errorMessage", "Please fix the errors in the form");
                        return "tickets/create";
                }

                try {
                        ticketService.createTicket(ticket);

                        //add to recent activity log
                        ticketService.addRecentActivity(0, ticket);
                        return "redirect:/tickets";
                } catch (ValidationException e) {
                        model.addAttribute("priorityValues", PriorityValue.values());
                        model.addAttribute("statusValues", StatusUpdates.values());
                        model.addAttribute("errorMessage", "Error creating ticket: " + e.getMessage());
                        return "tickets/create";
                }
        }

        // Edit existing ticket
        @PostMapping("/edit/{ticketId}")
        public String processEditForm(@PathVariable Integer ticketId,
                                      @Valid @ModelAttribute("ticket") Ticket ticket, // ModelAtt binds data to ticket object
                                      BindingResult bindingResult,
                                      Model model) {
                // Handles any errors stored in BindingResult object
                if (bindingResult.hasErrors()) {
                        model.addAttribute("priorityValues", PriorityValue.values());
                        model.addAttribute("statusValues", StatusUpdates.values());
                        model.addAttribute("errorMessage", "Please fix the errors in the form");
                        return "tickets/edit";
                }

                // try/catch block to intercept errors and keep the program running
                // Provides user with error message from TicketService injection from line #27 under Autowired
                try {
                        ticket.setTicketId(ticketId);
                        ticket.setLastEdited();

                        // Get current ticket first
                        Ticket currentTicket = ticketService.findTicketById(ticketId);

                        // If notes have changed, update them separately
                        if (currentTicket != null && !Objects.equals(currentTicket.getNotes(), ticket.getNotes())) {
                                ticketService.addOrUpdateNote(ticketId, ticket.getNotes());

                                //update recent activity log
                                ticketService.addRecentActivity(4,ticket);
                        }

                        //update recent activity log if status has changed
                        if (currentTicket != null && !Objects.equals(currentTicket.getStatus(), ticket.getStatus())) {
                                ticketService.addRecentActivity(2,ticket);

                        }
                        //update recent activity log if priority has changed
                        if (currentTicket != null && !Objects.equals(currentTicket.getPriority(), ticket.getPriority())) {
                                ticketService.addRecentActivity(3,ticket);

                        }

                        //update recent activity log if anything else has changed
                        if (currentTicket != null && (!Objects.equals(currentTicket.getName(), ticket.getName()) ||
                                !Objects.equals(currentTicket.getEmail(), ticket.getEmail()) ||
                                !Objects.equals(currentTicket.getDetails(), ticket.getDetails()) ||
                                !Objects.equals(currentTicket.getClientDepartment(), ticket.getClientDepartment()))){

                                ticketService.addRecentActivity(1,ticket);
                        }

                        ticketService.updateTicket(ticket);

                        return "redirect:/tickets";

                // Catches ValidationException from TicketService provided by injection
                } catch (ValidationException ve) {
                        model.addAttribute("errorMessage", "Invalid status transition: " + ve.getMessage());
                        model.addAttribute("priorityValues", PriorityValue.values());
                        model.addAttribute("statusValues", StatusUpdates.values());
                        return "tickets/edit";
                // Using Exception instead of ValidationException to catch all generic errors
                // Exception is a generic parent class in java, unlike ValidationException which was created in TicketService
                } catch (Exception e) {
                        model.addAttribute("errorMessage", "Error updating ticket: " + e.getMessage());
                        model.addAttribute("priorityValues", PriorityValue.values());
                        model.addAttribute("statusValues", StatusUpdates.values());
                        return "tickets/edit";
                }
        }

        @PostMapping
        public String populateTheTable(Model model){
                populateTable.populateATable();
                return "redirect:/tickets";
        }

                public void setTicketService (TicketService ticketService){
                        this.ticketService = ticketService;
                }
}
