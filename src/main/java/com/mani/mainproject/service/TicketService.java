package com.mani.mainproject.service;

import com.mani.mainproject.dto.CreateTicketRequest;
import com.mani.mainproject.dto.TicketResponse;

public interface TicketService {

    TicketResponse createTicket(CreateTicketRequest request);
}
