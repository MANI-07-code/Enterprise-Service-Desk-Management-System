package com.mani.mainproject.service;

import com.mani.mainproject.dto.LoginRequest;
import com.mani.mainproject.dto.LoginResponse;
import com.mani.mainproject.dto.RegisterRequest;
import com.mani.mainproject.dto.UserResponse;




public interface AuthService {

    UserResponse registerUser(RegisterRequest request);

    LoginResponse loginUser(LoginRequest request);
}
