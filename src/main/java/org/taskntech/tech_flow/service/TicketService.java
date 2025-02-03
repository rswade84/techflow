package org.taskntech.tech_flow.service;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.taskntech.tech_flow.data.TicketRepository;
import org.taskntech.tech_flow.exceptions.TicketNotFoundException;
import org.taskntech.tech_flow.models.PriorityValue;
import org.taskntech.tech_flow.models.ResponseTimeMetrics;
import org.taskntech.tech_flow.models.StatusUpdates;
import org.taskntech.tech_flow.models.Ticket;
import org.taskntech.tech_flow.notifications.TicketUpdatedEvent;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TicketService {

        @Autowired // Automatically injects an instance of TicketRepository
        private TicketRepository ticketRepository; // Provides access to ticket-related database operations

        // Publishes events to notify other components in the program
        // Final ensures that its initialized and can never be null
        private final ApplicationEventPublisher eventPublisher;

        // Constructor for injecting ApplicationEventPublisher
        public TicketService(ApplicationEventPublisher eventPublisher) {
                this.eventPublisher = eventPublisher;
        }

        // Add setter for testing
        public void setTicketRepository(TicketRepository ticketRepository) {
                this.ticketRepository = ticketRepository;
        }

        //list of value to be displayed for sort selector
        public List<String> sortOptions = List.of("Default","Oldest First", "Newest First", "Highest First","Lowest First","Closed Tickets");

        //Array list of recent activity in app holds up to 10
        public ArrayList<String> recentActivityLog = new ArrayList<>();

        // Create a new ticket, save it to the database via ticketRepository.save()
        public Ticket createTicket(Ticket ticket) {
                return ticketRepository.save(ticket);
        }

        // Read all tickets, return a list of tickets
        public List<Ticket> getAllTickets() { // returns a list type of all tickets called "getAllTickets"
                List<Ticket> tickets = new ArrayList<>(); // creating an empty list to store the tickets
                ticketRepository.findAll().forEach(ticket -> { // Iterates over repo/database, returns all tickets and adds them to the list
                        tickets.add(ticket);
                });
                return tickets;
        }

        // Sort ticket list
        public List<Ticket> getTicketList(String value){
                List<Ticket> tickets = new ArrayList<>();
                ticketRepository.findAll().forEach(ticket -> { // Iterates over repo/database, returns all tickets and adds them to the list
                        tickets.add(ticket);
                });

                //used to decided if resolved tickets should be removed from from tickets
                int flag = 0;

                //if oldest first is selected
                if (value.equals("Oldest First")){

                        //.sort() method for array list
                        //sorts items in tickets comparing two values at a time until list is sorted from oldest to youngest

                        //localDateTime functions:
                        //isBefore: t1.isbefore(t2) boolean that returns true if t1 is older than t2
                        //isAfter: t1.isAfter(t2) boolean that returns true if t1 is younger than t2

                        tickets.sort((t1, t2) -> {
                                if (t1.getDateSubmitted().isBefore(t2.getDateSubmitted())) {
                                        return -1; // keep items as is t1 should come before t2
                                } else if (t1.getDateSubmitted().isAfter(t2.getDateSubmitted())) {
                                        return 1; //switch items t2 should come before item t1
                                } else {
                                        return 0; // values are the same so no switch of positions
                                }
                        });

                //if newest first is selected
                } else if (value.equals("Newest First")) {

                        //.sort() method for array list
                        //sorts items in tickets comparing two values at a time until list is sorted from youngest to oldest

                        //localDateTime functions:
                        //isBefore: t1.isbefore(t2) boolean that returns true if t1 is older than t2
                        //isAfter: t1.isAfter(t2) boolean that returns true if t1 is younger than t2

                        tickets.sort((t1, t2) -> {
                                if (t1.getDateSubmitted().isAfter(t2.getDateSubmitted())) {
                                        return -1; // keep items as is t1 should come before t2
                                } else if (t1.getDateSubmitted().isBefore(t2.getDateSubmitted())) {
                                        return 1; //switch items t2 should come before item t1
                                } else {
                                        return 0;// values are the same so no switch of positions
                                }
                        });

                //if Highest first is selected
                }else if (value.equals("Highest First")) {

                        //.sort() method for array list
                        //sorts items in tickets comparing two values at a time until list is sorted from highest Priority to lowest

                        //get enum int value and compares to see which is higher or lower or equal
                        //t1.getPriority().getPriority() gets enum int value

                        tickets.sort((t1, t2) -> {
                                if (t1.getPriority().getPriority() > t2.getPriority().getPriority()) {
                                        return -1; // keep items as is t1 is higher priority than t2
                                } else if (t1.getPriority().getPriority() < t2.getPriority().getPriority()) {
                                        return 1; //switch items t2 should come before item t1
                                } else {
                                        return 0; // values are the same so no switch of positions
                                }
                        });

                }else if (value.equals( "Lowest First")) {

                        //.sort() method for array list
                        //sorts items in tickets comparing two values at a time until list is sorted from lowest Priority to highest

                        //get enum int value and compares to see which is higher or lower or equal
                        //t1.getPriority().getPriority() gets enum int value
                        tickets.sort((t1, t2) -> {
                                if (t1.getPriority().getPriority() < t2.getPriority().getPriority()) {
                                        return -1;// keep items as is t1 is lower priority than t2
                                } else if (t1.getPriority().getPriority() > t2.getPriority().getPriority()) {
                                        return 1; //switch items t2 should come before item t1
                                } else {
                                        return 0; // values are the same so no switch of positions
                                }
                        });

                //if closed tickets is selected
                }else if (value.equals("Closed Tickets")) {
                        //do not removed closed tickets from list
                        flag = 1;

                        //remove tickets from list that are not closed
                        tickets.removeIf( t -> t.getStatus() != StatusUpdates.CLOSED);

                }

                if (flag == 0){
                        //remove tickets from list that are closed
                        tickets.removeIf( t -> t.getStatus() == StatusUpdates.CLOSED);
                }
                return tickets;

        }


        // Get number of ticket entries in database
        private int getSize(){

                int counter = 0;
                for ( Ticket t : ticketRepository.findAll()){
                        counter++;
                }

                return counter;
        }

        // Update a ticket and publish a domain event
        public Ticket updateTicket(Ticket ticket) {
                if (ticketRepository.existsById(ticket.getTicketId())) {
                        Ticket updatedTicket = ticketRepository.save(ticket);

                        // Publish the domain event after saving the ticket
                        eventPublisher.publishEvent(new TicketUpdatedEvent(updatedTicket));

                        return updatedTicket;
                }
                throw new TicketNotFoundException("Ticket not found with ID: " + ticket.getTicketId());
        }

        // Delete a ticket
        public void closeTicket(Integer ticketId) {

                //change ticket status to closed
                updateTicketStatus(ticketId,StatusUpdates.CLOSED);
        }

        // Update ticket status with optional<> since it returns null or the ticket
        public Ticket updateTicketStatus(Integer ticketId, StatusUpdates newStatus) {
                // Fetch the ticket by its ticketId
                Optional<Ticket> retrievedTicket = ticketRepository.findById(ticketId);

                //value for activity note
                int num = 2; //ticket status was changed
                if (newStatus == StatusUpdates.CLOSED){
                        num = 5; //ticket was closed
                }

                // Check if the ticket exists
                if (retrievedTicket.isPresent()) {
                        Ticket ticket = retrievedTicket.get(); // assign the ticket object to variable ticket

                        // Validate status transition
                        if (!isValidStatusTransition(ticket.getStatus(), newStatus)) {
                                throw new ValidationException(" Opps, invalid status transition from " +
                                        ticket.getStatus() + " to " + newStatus);
                        }

                        // Store the previous status before updating
                        ticket.setPreviousStatus(ticket.getStatus());

                        // Update the status
                        ticket.setStatus(newStatus);
                        ticket.setStatusLastUpdated(LocalDateTime.now());
                        ticket.setLastEdited();

                        Ticket updatedTicket = ticketRepository.save(ticket);

                        // add action to recent activity dashboard
                        addRecentActivity(num,updatedTicket);

                        // Publish a domain event
                        eventPublisher.publishEvent(new TicketUpdatedEvent(updatedTicket));

                        return updatedTicket;
                } else {
                        throw new TicketNotFoundException("Ticket not found with ID: " + ticketId);
                }
        }

        // Add or update note
        public Ticket addOrUpdateNote(Integer ticketId, String note) {
                // Fetch the ticket by ID
                Optional<Ticket> retrievedTicket = ticketRepository.findById(ticketId);

                // Check if the ticket exists
                if (retrievedTicket.isPresent()) {
                        Ticket ticket = retrievedTicket.get();
                        ticket.setNotes(note); // Update the note
                        return ticketRepository.save(ticket);
                } else {
                        // throw a customer exception
                        throw new TicketNotFoundException("Ticket not found with ID: " + ticketId);
                }
        }

        // Update ticket priority
        public Ticket updateTicketPriority(Integer ticketId, PriorityValue newPriority) {
                // Fetch the ticket by its ticketId
                Optional<Ticket> retrievedTicket = ticketRepository.findById(ticketId);

                // Check if ticket exists
                if (retrievedTicket.isPresent()) {
                        Ticket ticket = retrievedTicket.get(); // assigning the ticket object to the ticket variable
                        ticket.setPriority(newPriority);
                        return ticketRepository.save(ticket);
                } else {
                        throw new TicketNotFoundException("Ticket not found with ID: " + ticketId);
                }
        }

        // Update client department
        public Ticket updateClientDepartment(Integer ticketId, String newClientDepartment) {
                // Fetch the ticket details
                Optional<Ticket> retrievedTicket = ticketRepository.findById(ticketId);

                // Check if ticket exist
                if (retrievedTicket.isPresent()) {
                        Ticket ticket = retrievedTicket.get(); // if present, we hold it under "ticket"
                        ticket.setClientDepartment(newClientDepartment);
                        return ticketRepository.save(ticket);
                } else {
                        throw new TicketNotFoundException("Ticket not found with ID: " + ticketId);
                }
        }

        // Update ticket details
        public Ticket updateTicketDetails(Integer ticketId, String newTicketDetails) {
                // Fetch the ticket details
                Optional<Ticket> retrievedTicket = ticketRepository.findById(ticketId);

                // Check if ticket exist
                if (retrievedTicket.isPresent()) {
                        Ticket ticket = retrievedTicket.get();
                        ticket.setDetails(newTicketDetails);
                        return ticketRepository.save(ticket);
                } else {
                        throw new TicketNotFoundException("Ticket not found with ID: " + ticketId);
                }
        }

        public Ticket findTicketById(Integer ticketId) {
                return ticketRepository.findById(ticketId).orElse(null);
        }

        // When user change status, returns a boolean that handles all StatusUpdate ENUMS and checks if they are valid
        public boolean isValidStatusTransition(StatusUpdates currentStatus, StatusUpdates newStatus) {

                switch (currentStatus) {
                        case NOT_STARTED: // If the current status is NOT_STARTED
                                // Allows transition to (CLOSED or IN_PROGRESS)
                                return newStatus == StatusUpdates.CLOSED ||
                                        newStatus == StatusUpdates.IN_PROGRESS;

                        // 3 possible transitions. (CLOSED, DELAYED or RESOLVED)
                        case IN_PROGRESS:
                                return newStatus == StatusUpdates.CLOSED ||
                                        newStatus == StatusUpdates.DELAYED ||
                                        newStatus == StatusUpdates.RESOLVED;

                        // 3 possible transitions. (CLOSED, IN_PROGRESS, or RESOLVED)
                        case DELAYED:
                                return newStatus == StatusUpdates.CLOSED ||
                                        newStatus == StatusUpdates.IN_PROGRESS ||
                                        newStatus == StatusUpdates.RESOLVED;

                        // Allows user to re-open ticket, if needed.
                        case RESOLVED:
                                return newStatus == StatusUpdates.CLOSED ||
                                        newStatus == StatusUpdates.IN_PROGRESS;

                        // 1 possible transition to CLOSED, can't be re-opened.
                        case CLOSED:
                                return false;
                }
                return false;

        }

        public void addRecentActivity(int n, Ticket t){
                //specific string is chosen based on int n

                String entry = "";
                switch(n){
                        case 0: //ticket was created
                                entry = "Ticket No. " + t.getTicketId() + " was created.";
                                break;
                        case 1:// ticket was edited
                                entry = "Ticket No. " + t.getTicketId() + " was edited.";
                                break;
                        case 2: //ticket's status was changed
                                entry = "Ticket No. " + t.getTicketId() + " status was changed to " + t.getStatus() + ".";
                                break;
                        case 3: //ticket's priority was changed
                                entry = "Ticket No. " + t.getTicketId() + " priority was changed to " + t.getPriority() + ".";
                                break;
                        case 4: //ticket's notes were changed
                                entry = "Ticket No. " + t.getTicketId() + " notes were updated.";
                                break;
                        case 5:// ticket was closed
                                entry = "Ticket No. " + t.getTicketId() + " was closed.";
                                break;


                }
                //adds string entry to recentActivitylog
                updateActivityLog(entry);
        }

        public void updateActivityLog(String log){
                // if arraylist is larger than or equal to 10
                if (recentActivityLog.size() >= 10){

                        //wont every be larger than 10 so just remove last item
                        recentActivityLog.remove(9);

                        //add new string to first position and move the rest to the next index
                        recentActivityLog.add(0,log);

                }else {
                        //add new string to first position and move the rest to the next index
                        recentActivityLog.add(0,log);
                }
        }

        public double getAverageInitialResponseTime() {
                // Store all tickets - Lazy loading (Iterable loads what is needed only)
                Iterable<Ticket> tickets = ticketRepository.findAll();
                double totalHours = 0;
                int count = 0;

                // Use of for-each loop to iterate through tickets
                for (Ticket ticket : tickets) {
                        if (ticket.getStatusLastUpdated() != null &&
                                ticket.getDateSubmitted() != null &&
                                ticket.getStatus() != StatusUpdates.NOT_STARTED) {

                                // then calculate the duration
                                Duration duration = Duration.between(
                                        ticket.getDateSubmitted(),
                                        ticket.getStatusLastUpdated()
                                );
                                totalHours += duration.toHours();
                                count++;
                        }
                }
                return count > 0 ? totalHours / count : 0.0;
        }

        public double getAverageResolutionTime() {
                Iterable<Ticket> tickets = ticketRepository.findAll();
                double totalHours = 0;
                int count = 0;

                for (Ticket ticket : tickets) {
                        if (ticket.getStatus() == StatusUpdates.RESOLVED ||
                                ticket.getStatus() == StatusUpdates.CLOSED) {
                                if (ticket.getStatusLastUpdated() != null &&
                                        ticket.getDateSubmitted() != null) {

                                        // then calculate the duration
                                        Duration duration = Duration.between(
                                                ticket.getDateSubmitted(),
                                                ticket.getStatusLastUpdated()
                                        );
                                        totalHours += duration.toHours();
                                        count++;
                                }
                        }
                }
                return count > 0 ? totalHours / count : 0.0;
        }

        public ResponseTimeMetrics getResponseTimeMetrics() {
                // Calculate average response time from two other methods
                return new ResponseTimeMetrics(
                        getAverageInitialResponseTime(),
                        getAverageResolutionTime()
                );
        }

        // Filters tickets based on their status before grouping by priority. Excludes tickets where the status is marked as "closed" and "resolved".
        public Map<String, Long> getTicketCountByPriority() {
                return StreamSupport.stream(ticketRepository.findAll().spliterator(), false)
                        .filter(ticket -> ticket.getStatus() != StatusUpdates.CLOSED && ticket.getStatus() != StatusUpdates.RESOLVED)
                        .collect(Collectors.groupingBy(
                                ticket -> ticket.getPriority().name(),
                                Collectors.counting()
                        ));
        }

        // Added method to get tickets counts based on their respective status.
        public Map<String, Long> getTicketCountsByStatus() {
                return StreamSupport.stream(ticketRepository.findAll().spliterator(), false)
                        .collect(Collectors.groupingBy(
                                ticket -> ticket.getStatus().name(),
                                Collectors.counting()
                        ));
        }
}