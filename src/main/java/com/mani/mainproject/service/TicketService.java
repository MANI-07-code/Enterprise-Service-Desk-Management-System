package com.mani.mainproject.service;

import com.mani.mainproject.dto.CreateTicketRequest;
import com.mani.mainproject.dto.TicketResponse;

import java.util.List;

public interface TicketService {

    TicketResponse createTicket(CreateTicketRequest request);

    List<TicketResponse> getMyTickets();
}
