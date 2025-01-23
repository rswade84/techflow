package org.taskntech.tech_flow.notifications;

import org.taskntech.tech_flow.models.Ticket;

import java.security.Principal;

public class TicketUpdatedEvent {

    private final Ticket ticket;

    public TicketUpdatedEvent(Ticket ticket){
        this.ticket = ticket;
    }

    public Ticket getTicket(){
        return ticket;
    }
}
