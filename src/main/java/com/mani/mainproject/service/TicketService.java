package com.mani.mainproject.service;

import com.mani.mainproject.dto.*;

import java.util.List;

public interface TicketService {

    TicketResponse createTicket(CreateTicketRequest request);

    List<TicketResponse> getMyTickets();

    List<TicketResponse> getAllTickets();

    String assignTicket(Long ticketId, AssignTicketRequest request);

    List<TicketResponse> getAssignedTickets();

    String updateTicketStatus(Long ticketId, UpdateTicketStatusRequest request);

    String closeTicket(Long ticketId);

    String changeTicketPriority(Long ticketId , ChangePriorityRequest request);
}
