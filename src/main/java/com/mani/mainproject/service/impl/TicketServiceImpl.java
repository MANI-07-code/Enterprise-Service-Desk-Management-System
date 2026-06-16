package com.mani.mainproject.service.impl;

import com.mani.mainproject.dto.AssignTicketRequest;
import com.mani.mainproject.dto.CreateTicketRequest;
import com.mani.mainproject.dto.TicketResponse;
import com.mani.mainproject.entity.Ticket;
import com.mani.mainproject.entity.User;
import com.mani.mainproject.enums.Role;
import com.mani.mainproject.enums.TicketPriority;
import com.mani.mainproject.enums.TicketStatus;
import com.mani.mainproject.exception.BadRequestException;
import com.mani.mainproject.exception.ResourceNotFoundException;
import com.mani.mainproject.repository.TicketRepository;
import com.mani.mainproject.repository.UserRepository;
import com.mani.mainproject.service.TicketService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {


    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    @Override
    public TicketResponse createTicket(CreateTicketRequest request){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setPriority(TicketPriority.MEDIUM);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setCreatedBy(user);
        ticket.setAssignedTo(null);

        Ticket savedTicket = ticketRepository.save(ticket);

        return convertToResponse(savedTicket);
    }

    @Override
    public List<TicketResponse> getMyTickets(){
        List<TicketResponse> responses =new ArrayList<>();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();



        List<Ticket> tickets = ticketRepository.findByCreatedBy(user);

       for(Ticket ticket : tickets){
           responses.add(convertToResponse(ticket));
       }
      return responses;
    }

    @Override
    public List<TicketResponse> getAllTickets(){
        List<Ticket> tickets = ticketRepository.findAll();

        List<TicketResponse> responses = new ArrayList<>();
        for(Ticket ticket : tickets){
            responses.add(convertToResponse(ticket));
        }
        return responses;
    }

    @Override
    public String assignTicket(Long ticketId , AssignTicketRequest request){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        User engineer = userRepository.findById(request.getSupportEngineerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(engineer.getRole() != Role.SUPPORT_ENGINEER){
            throw new BadRequestException("Selected user is not SUPPORT_ENGINEER");
        }

        if(ticket.getStatus() == TicketStatus.RESOLVED
                || ticket.getStatus() == TicketStatus.CLOSED){

            throw new BadRequestException(
                    "Ticket cannot be assigned");
        }
        ticket.setAssignedTo(engineer);
        ticket.setStatus(TicketStatus.ASSIGNED);
        ticketRepository.save(ticket);
        return "Ticket assigned successfully";
    }

    private TicketResponse convertToResponse(
            Ticket ticket){

        TicketResponse response =new TicketResponse();
        response.setTitle(ticket.getTitle());
        response.setDescription(ticket.getDescription());
        response.setTicketId(ticket.getTicketId());
        response.setStatus(ticket.getStatus());
        response.setPriority(ticket.getPriority());
        response.setCreatedAt(ticket.getCreatedAt());
        response.setCreatedById(ticket.getCreatedBy().getUserId());
        response.setCreatedByName(ticket.getCreatedBy().getUserName());
        if(ticket.getAssignedTo() != null){

            response.setAssignedToId(ticket.getAssignedTo().getUserId());

            response.setAssignedToName(ticket.getAssignedTo().getUserName());
        }

        return response;
    }

}
