package com.mani.mainproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDashboardResponse {

    private long totalUsers;
    private long activeUsers;
    private long inactiveUsers;
    private long totalTickets;

}
