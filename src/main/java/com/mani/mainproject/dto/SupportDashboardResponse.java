package com.mani.mainproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportDashboardResponse {

    private long assignedTickets;
    private long inProgressTickets;
    private long resolvedTickets;
}
