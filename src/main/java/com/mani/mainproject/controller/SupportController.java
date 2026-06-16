package com.mani.mainproject.controller;

import com.mani.mainproject.dto.TicketResponse;
import com.mani.mainproject.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/support")
public class SupportController {

    private final TicketService ticketService;

    @GetMapping("/tickets")
    public List<TicketResponse> getAssignedTickets(){
        return ticketService.getAssignedTickets();
    }


}
