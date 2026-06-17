package com.mani.mainproject.service;

import com.mani.mainproject.dto.AdminDashboardResponse;
import com.mani.mainproject.dto.ChangeRoleRequest;
import com.mani.mainproject.dto.ChangeStatusRequest;
import com.mani.mainproject.dto.UserResponse;

import java.util.List;

public interface UserService {

    String changeUserRole(Long userId, ChangeRoleRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long userId);

    String changeUserStatus(Long userId, ChangeStatusRequest request);

    AdminDashboardResponse getAdminDashboard();
}
