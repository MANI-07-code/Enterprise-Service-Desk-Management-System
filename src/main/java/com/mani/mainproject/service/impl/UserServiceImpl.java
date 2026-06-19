package com.mani.mainproject.service.impl;

import com.mani.mainproject.dto.*;
import com.mani.mainproject.entity.User;
import com.mani.mainproject.enums.Role;
import com.mani.mainproject.enums.TicketStatus;
import com.mani.mainproject.enums.UserStatus;
import com.mani.mainproject.exception.ResourceNotFoundException;
import com.mani.mainproject.repository.TicketRepository;
import com.mani.mainproject.repository.UserRepository;
import com.mani.mainproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final static Logger logger =LoggerFactory.getLogger(UserServiceImpl.class);

    private final TicketRepository ticketRepository;

    @Override
    public String changeUserRole(Long userId, ChangeRoleRequest request){

        User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
        Role oldRole = user.getRole();
        user.setRole(request.getRole());
        userRepository.save(user);

        logger.info(
                "Admin {} changed User {} role from {} to {}",
                admin.getUserId(),
                user.getUserId(),
                oldRole,
                request.getRole()
        );

       return "Role updated successfully";
    }

@Override
    public List<UserResponse> getAllUsers(){

        List<User> users =userRepository.findAll();
        List<UserResponse> responses = new ArrayList<>();
        for(User user : users){
            responses.add(convertToResponse(user));
        }
         return responses;
    }

    @Override
    public UserResponse getUserById(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertToResponse(user);
    }

    @Override
    public String changeUserStatus(Long userId, ChangeStatusRequest request){

        User admin =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
        UserStatus oldStatus = user.getStatus();
        user.setStatus(request.getStatus());
        userRepository.save(user);

        logger.info(
                "Admin {} changed User {} status from {} to {}",
                admin.getUserId(),
                user.getUserId(),
                oldStatus,
                request.getStatus()
        );
        return "Status updated successfully";
    }

    @Override
    public AdminDashboardResponse getAdminDashboard(){
        AdminDashboardResponse response = new AdminDashboardResponse();
        response.setTotalUsers(userRepository.count());
        response.setActiveUsers(userRepository.countByStatus(UserStatus.ACTIVE));
        response.setInactiveUsers(userRepository.countByStatus(UserStatus.INACTIVE));
        response.setTotalTickets(ticketRepository.count());
        return response;
    }


    private UserResponse convertToResponse(User user){
        UserResponse response =new UserResponse();
        response.setUserId(user.getUserId());
        response.setUserName(user.getUserName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        return response;
    }

}
