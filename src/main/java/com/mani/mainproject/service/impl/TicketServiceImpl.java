package com.mani.mainproject.service.impl;

import com.mani.mainproject.dto.*;
import com.mani.mainproject.entity.Ticket;
import com.mani.mainproject.entity.User;
import com.mani.mainproject.enums.Role;
import com.mani.mainproject.enums.TicketPriority;
import com.mani.mainproject.enums.TicketStatus;
import com.mani.mainproject.exception.BadRequestException;
import com.mani.mainproject.exception.ForbiddenException;
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

    @Override
    public List<TicketResponse> getAssignedTickets(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Ticket> tickets = ticketRepository.findByAssignedTo(user);
        List<TicketResponse> responses = new ArrayList<>();

        for(Ticket ticket : tickets){
            responses.add(convertToResponse(ticket));
        }

        return responses;
    }

    @Override
    public String updateTicketStatus(Long ticketId , UpdateTicketStatusRequest request){

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Invalid ticket"));

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(ticket.getAssignedTo() == null ||
                !ticket.getAssignedTo().getUserId().equals(user.getUserId())){
            throw new BadRequestException("Ticket is not assigned to you");
        }

        if(ticket.getStatus() == TicketStatus.ASSIGNED
        && request.getStatus() == TicketStatus.IN_PROGRESS){

            ticket.setStatus(TicketStatus.IN_PROGRESS);
        }

        else if(ticket.getStatus() == TicketStatus.IN_PROGRESS
        && request.getStatus() == TicketStatus.RESOLVED){

            ticket.setStatus(TicketStatus.RESOLVED);
        }
        else {
            throw new BadRequestException("Invalid status transition");
        }

        ticketRepository.save(ticket);
return "Ticket status updated successfully";
    }

    @Override
    public String closeTicket(Long ticketId){

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!ticket.getCreatedBy().getUserId().equals(user.getUserId())){

            throw new BadRequestException("you only change your tickets");
        }

        if(!ticket.getStatus().equals(TicketStatus.RESOLVED)){
            throw new BadRequestException("Invalid status transition");
        }
        ticket.setStatus(TicketStatus.CLOSED);

        ticketRepository.save(ticket);

        return "Ticket closed successfully";


    }

    @Override
    public String changeTicketPriority(Long ticketId , ChangePriorityRequest request){

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        if(ticket.getStatus() == TicketStatus.CLOSED){
            throw new BadRequestException("cannot change priority of closed ticket");
        }
        ticket.setPriority(request.getPriority());
        ticketRepository.save(ticket);

        return "Priority updated successfully";
    }

    @Override
    public TicketResponse getTicketById(Long ticketId){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        User user =(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        if(user.getRole() == Role.MANAGER || user.getRole() == Role.ADMIN ){
            return convertToResponse(ticket);
        }

        if(user.getRole() == Role.EMPLOYEE){

            if(ticket.getCreatedBy()
                    .getUserId()
                    .equals(user.getUserId())){

                return convertToResponse(ticket);
            }
        }

        if(user.getRole() == Role.SUPPORT_ENGINEER){

            if(ticket.getAssignedTo() != null &&
                    ticket.getAssignedTo()
                            .getUserId()
                            .equals(user.getUserId())){

                return convertToResponse(ticket);
            }
        }

        throw new ForbiddenException("Access denied");

    }

    @Override
    public ManagerDashboardResponse getManagerDashboard(){

        ManagerDashboardResponse response = new ManagerDashboardResponse();
        response.setOpenTickets(ticketRepository.countByStatus(TicketStatus.OPEN));
        response.setAssignedTickets(ticketRepository.countByStatus(TicketStatus.ASSIGNED));
        response.setInProgressTickets(ticketRepository.countByStatus(TicketStatus.IN_PROGRESS));
        response.setResolvedTickets(ticketRepository.countByStatus(TicketStatus.RESOLVED));
        response.setClosedTickets(ticketRepository.countByStatus(TicketStatus.CLOSED));

        return response;
    }

@Override
public SupportDashboardResponse getSupportDashboard(){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SupportDashboardResponse response = new SupportDashboardResponse();
        response.setAssignedTickets(ticketRepository.countByAssignedToAndStatus(
                user, TicketStatus.ASSIGNED));

        response.setInProgressTickets(ticketRepository.countByAssignedToAndStatus(
                user,TicketStatus.IN_PROGRESS));

        response.setResolvedTickets(ticketRepository.countByAssignedToAndStatus(
                user,
                TicketStatus.RESOLVED));

        return response;

}

    @Override
    public EmployeeDashboardResponse getEmployeeDashboard(){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EmployeeDashboardResponse response = new EmployeeDashboardResponse();

        response.setOpenTickets(ticketRepository.countByAssignedToAndStatus(user, TicketStatus.OPEN));
        response.setResolvedTickets(ticketRepository.countByAssignedToAndStatus(user,TicketStatus.RESOLVED));
        response.setClosedTickets(ticketRepository.countByAssignedToAndStatus(user,TicketStatus.CLOSED));

        return response;
    }

    private TicketResponse convertToResponse(Ticket ticket){

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
