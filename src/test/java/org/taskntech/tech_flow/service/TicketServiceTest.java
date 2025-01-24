package org.taskntech.tech_flow.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.taskntech.tech_flow.data.TicketRepository;
import org.taskntech.tech_flow.models.PriorityValue;
import org.taskntech.tech_flow.models.Ticket;
import org.taskntech.tech_flow.exceptions.TicketNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

        @Mock
        private TicketRepository ticketRepository;

        @InjectMocks
        private TicketService ticketService;

        private Ticket testTicket;

        @BeforeEach
        void setUp() {
                testTicket = new Ticket("Test User", "test@email.com", "Test Details", PriorityValue.LOW, "IT");
                testTicket.setTicketId(1);
        }

        @Test
        void addOrUpdateNote_ShouldUpdateNote() {
                // Arrange
                String newNote = "Important update for ticket here";
                when(ticketRepository.findById(1)).thenReturn(Optional.of(testTicket));
                when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);

                // Act
                Ticket updatedTicket = ticketService.addOrUpdateNote(1, newNote);

                // Assert
                assertEquals(newNote, updatedTicket.getNotes());
        }

        @Test
        void addOrUpdateNote_ShouldThrowExceptionForNonexistentTicket() {
                // Arrange
                when(ticketRepository.findById(999)).thenReturn(Optional.empty());

                // Act & Assert
                assertThrows(TicketNotFoundException.class, () -> {
                        ticketService.addOrUpdateNote(999, "New note");
                });
        }

        @Test
        void updateTicket_ShouldKeepExistingNotes() {
                // Arrange
                String existingNote = "Existing note";
                testTicket.setNotes(existingNote);
                when(ticketRepository.findById(1)).thenReturn(Optional.of(testTicket));
                when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);

                // Create a new ticket with same ID but no notes
                Ticket updateTicket = new Ticket();
                updateTicket.setTicketId(1);

                // Act
                Ticket result = ticketService.updateTicket(updateTicket);

                // Assert
                assertEquals(existingNote, result.getNotes(), "Notes should be preserved after update");
        }
}