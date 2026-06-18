package com.mani.mainproject.repository;

import com.mani.mainproject.entity.Ticket;
import com.mani.mainproject.entity.User;
import com.mani.mainproject.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {

    List<Ticket> findByCreatedBy(User user);
    List<Ticket> findByAssignedTo(User user);

    long countByStatus(TicketStatus status);

    long countByAssignedToAndStatus(User assignedTo , TicketStatus status);
}
