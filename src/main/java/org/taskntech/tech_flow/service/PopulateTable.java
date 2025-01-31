package org.taskntech.tech_flow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.taskntech.tech_flow.data.TicketRepository;
import org.taskntech.tech_flow.models.PriorityValue;
import org.taskntech.tech_flow.models.StatusUpdates;
import org.taskntech.tech_flow.models.Ticket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PopulateTable {

    @Autowired // Used to inject dependencies
    private TicketRepository ticketRepository;

    public List<Ticket> firstTickets = new ArrayList<>();

    public  void populatelist() {
        // Ticket 1
        Ticket ticket1 = new Ticket(
                "Emma Watson", "emma.watson@company.com",
                LocalDateTime.parse("2024-01-06 10:30:12.654321", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // statusLastUpdated
                StatusUpdates.NOT_STARTED,
                LocalDateTime.parse("2024-01-05 09:15:32.123456", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // lastEdited
                "Laptop screen flickering",
                PriorityValue.HIGH,
                StatusUpdates.IN_PROGRESS,
                LocalDateTime.parse("2024-01-05 09:15:32.123456", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // dateSubmitted
                "IT Dept", "Ordered replacement screen." // "IT Dept" is now between 3 and 15 characters
        );
        firstTickets.add(ticket1);

        // Ticket 2
        Ticket ticket2 = new Ticket(
                "Liam Johnson", "liam.johnson@business.com",
                LocalDateTime.parse("2024-02-13 12:45:00.123456", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // statusLastUpdated
                StatusUpdates.DELAYED,
                LocalDateTime.parse("2024-02-10 14:20:45.654321", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // lastEdited
                "HR portal login issues",
                PriorityValue.MEDIUM,
                StatusUpdates.RESOLVED,
                LocalDateTime.parse("2024-02-10 14:20:45.654321", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // dateSubmitted
                "HR Dept", "Password reset performed." // "HR Dept" is fine
        );
        firstTickets.add(ticket2);

        // Ticket 3
        Ticket ticket3 = new Ticket(
                "Sophia Lee", "sophia.lee@finance.org",
                LocalDateTime.parse("2024-03-16 14:10:50.234567", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // statusLastUpdated
                StatusUpdates.NOT_STARTED,
                LocalDateTime.parse("2024-03-15 08:05:20.234567", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // lastEdited
                "Accounting software crashes frequently",
                PriorityValue.HIGH,
                StatusUpdates.IN_PROGRESS,
                LocalDateTime.parse("2024-03-15 08:05:20.234567", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // dateSubmitted
                "Finance Dept", "Patch update scheduled." // "Finance Dept" is fine
        );
        firstTickets.add(ticket3);

        // Ticket 4
        Ticket ticket4 = new Ticket(
                "Noah Wilson", "noah.wilson@company.net",
                LocalDateTime.parse("2024-04-20 10:45:40.567890", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // statusLastUpdated
                StatusUpdates.NOT_STARTED,
                null, // lastEdited (null, no date)
                "Printer not responding",
                PriorityValue.LOW,
                StatusUpdates.DELAYED,
                LocalDateTime.parse("2024-04-20 10:45:40.567890", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // dateSubmitted
                "Marketing", "Waiting for technician visit." // "Marketing" is fine
        );
        firstTickets.add(ticket4);

        // Ticket 5
        Ticket ticket5 = new Ticket(
                "Isabella Martinez", "isabella.martinez@tech.com",
                LocalDateTime.parse("2024-05-27 17:00:00.654321", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // statusLastUpdated
                StatusUpdates.RESOLVED,
                LocalDateTime.parse("2024-05-25 13:20:10.345678", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // lastEdited
                "Software license expired",
                PriorityValue.MEDIUM,
                StatusUpdates.CLOSED,
                LocalDateTime.parse("2024-05-25 13:20:10.345678", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // dateSubmitted
                "IT Support", "Renewed license and updated system." // "IT Support" is fine
        );
        firstTickets.add(ticket5);

        // Ticket 6
        Ticket ticket6 = new Ticket(
                "Ethan Thomas", "ethan.thomas@logistics.com",
                LocalDateTime.parse("2024-07-02 14:20:18.987654", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // statusLastUpdated
                StatusUpdates.IN_PROGRESS,
                LocalDateTime.parse("2024-06-30 12:10:22.987654", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // lastEdited
                "Scanner not detecting barcodes",
                PriorityValue.HIGH,
                StatusUpdates.RESOLVED,
                LocalDateTime.parse("2024-06-30 12:10:22.987654", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // dateSubmitted
                "Operations", "Recalibrated scanner." // "Operations" is fine
        );
        firstTickets.add(ticket6);

        // Ticket 7
        Ticket ticket7 = new Ticket(
                "Ava Roberts", "ava.roberts@sales.org",
                LocalDateTime.parse("2024-08-02 16:30:40.543210", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // statusLastUpdated
                null,
                LocalDateTime.parse("2024-08-01 15:50:30.765432", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // lastEdited
                "CRM not loading new leads",
                PriorityValue.MEDIUM,
                StatusUpdates.NOT_STARTED,
                LocalDateTime.parse("2024-08-01 15:50:30.765432", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // dateSubmitted
                "Sales Dept", "Issue escalated to vendor." // "Sales Dept" is fine
        );
        firstTickets.add(ticket7);

        // Ticket 8
        Ticket ticket8 = new Ticket(
                "William Carter", "william.carter@lawfirm.com",
                LocalDateTime.parse("2024-09-10 14:25:55.543210", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // statusLastUpdated
                StatusUpdates.IN_PROGRESS,
                null, // lastEdited (null, no date)
                "Document management system error",
                PriorityValue.LOW,
                StatusUpdates.DELAYED,
                LocalDateTime.parse("2024-09-10 14:25:55.543210", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // dateSubmitted
                "Legal Dept", "Fix deployed, awaiting verification." // "Legal Dept" is fine
        );
        firstTickets.add(ticket8);

        // Ticket 9
        Ticket ticket9 = new Ticket(
                "Olivia Anderson", "olivia.anderson@company.org",
                LocalDateTime.parse("2024-10-17 14:10:00.987654", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // statusLastUpdated
                StatusUpdates.NOT_STARTED,
                LocalDateTime.parse("2024-10-15 11:40:33.678901", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // lastEdited
                "Slow network connectivity",
                PriorityValue.HIGH,
                StatusUpdates.RESOLVED,
                LocalDateTime.parse("2024-10-15 11:40:33.678901", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // dateSubmitted
                "IT Support", "Replaced faulty switch." // "IT Support" is fine
        );
        firstTickets.add(ticket9);

        // Ticket 10
        Ticket ticket10 = new Ticket(
                "James White", "james.white@support.com",
                LocalDateTime.parse("2025-01-20 17:55:22.234568", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // statusLastUpdated
                StatusUpdates.NOT_STARTED,
                null, // lastEdited (null, no date)
                "Headset not working",
                PriorityValue.LOW,
                StatusUpdates.IN_PROGRESS,
                LocalDateTime.parse("2025-01-20 17:55:22.234568", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")), // dateSubmitted
                "Support Dept", "Ordered replacement headset." // "Support Dept" is between 3 and 15 characters
        );
        firstTickets.add(ticket10);
    }

    public void populateATable(){
        populatelist();

        for (Ticket t:firstTickets){
            ticketRepository.save(t);
        }
    }
}
