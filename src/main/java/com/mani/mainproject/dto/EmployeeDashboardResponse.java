package com.mani.mainproject.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeeDashboardResponse {
    private long openTickets;
    private long resolvedTickets;
    private long closedTickets;
}
