package com.mani.mainproject.service.impl;

import com.mani.mainproject.dto.LoginRequest;
import com.mani.mainproject.dto.LoginResponse;
import com.mani.mainproject.dto.RegisterRequest;
import com.mani.mainproject.dto.UserResponse;
import com.mani.mainproject.entity.User;
import com.mani.mainproject.enums.Role;
import com.mani.mainproject.enums.UserStatus;
import com.mani.mainproject.exception.BadRequestException;
import com.mani.mainproject.exception.ForbiddenException;
import com.mani.mainproject.exception.ResourceNotFoundException;
import com.mani.mainproject.repository.UserRepository;
import com.mani.mainproject.security.JwtService;
import com.mani.mainproject.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;//constructor injection

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;

        this.passwordEncoder=passwordEncoder;

        this.jwtService = jwtService;
    }

    @Override
    public UserResponse registerUser(RegisterRequest request){

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new BadRequestException("email already exists");
        }
       User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.EMPLOYEE);
        user.setStatus(UserStatus.ACTIVE);
          userRepository.save(user);

          UserResponse response = new UserResponse();
          response.setUserId(user.getUserId());
          response.setUserName(user.getUserName());
          response.setEmail(user.getEmail());
          response.setRole(user.getRole());

        return response;

    }

    @Override
    public LoginResponse loginUser(LoginRequest request){

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){

            throw new BadRequestException("invalid password ");
        }

        if(user.getStatus() == UserStatus.INACTIVE){
            throw new ForbiddenException("Account is inactive");
        }


//        boolean match = passwordEncoder.matches(
//                request.getPassword(),
//                user.getPassword());
//
//        System.out.println("Password Match = " + match);
//
//        if(!match){
//            System.out.println("Throwing BadRequestException");
//            throw new BadRequestException("invalid password ");
//        }

        String token = jwtService.generateToken(user);

        LoginResponse response = new LoginResponse();

            response.setToken(token);
            response.setUserId(user.getUserId());
            response.setUserName(user.getUserName());
            response.setEmail(user.getEmail());
            response.setRole(user.getRole());



        return response;
    }
}
