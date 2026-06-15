package com.mani.mainproject.service.impl;

import com.mani.mainproject.dto.ChangeRoleRequest;
import com.mani.mainproject.entity.User;
import com.mani.mainproject.enums.Role;
import com.mani.mainproject.exception.ResourceNotFoundException;
import com.mani.mainproject.repository.UserRepository;
import com.mani.mainproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void changeUserRole(Long userId, ChangeRoleRequest request){

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
        user.setRole(request.getRole());
        userRepository.save(user);

    }
}
