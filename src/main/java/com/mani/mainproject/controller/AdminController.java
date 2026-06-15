package com.mani.mainproject.controller;

import com.mani.mainproject.dto.ChangeRoleRequest;
import com.mani.mainproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;

    @PutMapping("/users/{id}/role")
    public void changeRole(@PathVariable Long id, @RequestBody ChangeRoleRequest request) {

        userService.changeUserRole(id, request);
    }


}
