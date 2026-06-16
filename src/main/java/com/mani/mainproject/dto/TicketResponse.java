package com.mani.mainproject.dto;

import com.mani.mainproject.enums.TicketPriority;
import com.mani.mainproject.enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class TicketResponse {

    private Long ticketId;

    private String title;

    private String description;

    private TicketStatus status;

    private TicketPriority priority;

    private LocalDateTime createdAt;

    private Long createdById;
    private String createdByName;

    private Long assignedToId;
    private String assignedToName;
}
