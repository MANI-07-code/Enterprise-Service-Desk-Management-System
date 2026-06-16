package com.mani.mainproject.controller;

import com.mani.mainproject.dto.CreateTicketRequest;
import com.mani.mainproject.dto.TicketResponse;
import com.mani.mainproject.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public TicketResponse createTicket(@RequestBody CreateTicketRequest request){

        return ticketService.createTicket(request);
    }

    @GetMapping("/my")
    public List<TicketResponse> getMyTickets(){
        return ticketService.getMyTickets();
    }

}