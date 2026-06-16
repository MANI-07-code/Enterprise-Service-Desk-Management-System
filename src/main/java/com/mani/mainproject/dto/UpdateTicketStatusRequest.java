package com.mani.mainproject.dto;

import com.mani.mainproject.enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTicketStatusRequest {

    private TicketStatus status;

}
