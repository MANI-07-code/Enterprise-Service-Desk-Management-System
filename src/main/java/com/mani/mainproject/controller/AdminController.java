package com.mani.mainproject.controller;

import com.mani.mainproject.dto.ChangeRoleRequest;
import com.mani.mainproject.dto.ChangeStatusRequest;
import com.mani.mainproject.dto.UserResponse;
import com.mani.mainproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;

    @PutMapping("/users/{id}/role")
    public String changeRole(@PathVariable Long id, @RequestBody ChangeRoleRequest request) {

       return  userService.changeUserRole(id, request);
    }
    @GetMapping("/users")
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")

    public UserResponse getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PutMapping("/users/{id}/status")
    public String changeStatus(@PathVariable Long id , @RequestBody ChangeStatusRequest request){
        return userService.changeUserStatus(id,request);
    }
}
