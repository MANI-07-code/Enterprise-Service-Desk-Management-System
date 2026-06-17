package com.mani.mainproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboardResponse {

    private Long openTickets;
    private Long assignedTickets;
    private Long inProgressTickets;
    private Long resolvedTickets;
    private Long closedTickets;

}
