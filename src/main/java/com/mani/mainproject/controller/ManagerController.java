package com.mani.mainproject.controller;

import com.mani.mainproject.dto.AssignTicketRequest;
import com.mani.mainproject.dto.TicketResponse;
import com.mani.mainproject.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final TicketService ticketService;

    @GetMapping("/tickets")
    public List<TicketResponse> getAllTickets(){
        return ticketService.getAllTickets();
    }

    @PutMapping("/tickets/{id}/assign")
    public String assignTicket(
            @PathVariable Long id,
            @RequestBody AssignTicketRequest request){

        return ticketService.assignTicket(id, request);
    }


}
