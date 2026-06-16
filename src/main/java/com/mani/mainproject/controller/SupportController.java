package com.mani.mainproject.controller;

import com.mani.mainproject.dto.TicketResponse;
import com.mani.mainproject.dto.UpdateTicketStatusRequest;
import com.mani.mainproject.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/tickets/{id}/status")
    public String updateTicketStatus(@PathVariable Long id , @RequestBody UpdateTicketStatusRequest request){

        return ticketService.updateTicketStatus(id,request);
    }

}
