package com.mani.mainproject.controller;

import com.mani.mainproject.dto.LoginRequest;
import com.mani.mainproject.dto.LoginResponse;
import com.mani.mainproject.dto.RegisterRequest;
import com.mani.mainproject.dto.UserResponse;
import com.mani.mainproject.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public UserResponse registerUser(@Valid @RequestBody RegisterRequest request){
        return authService.registerUser(request);
    }


    @PostMapping("/login")
    public LoginResponse loginUser(@Valid @RequestBody LoginRequest request){
        return authService.loginUser(request);
    }
}
