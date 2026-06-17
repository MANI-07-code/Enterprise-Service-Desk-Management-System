package com.mani.mainproject.dto;

import com.mani.mainproject.enums.TicketPriority;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePriorityRequest {

    private TicketPriority priority;
}
