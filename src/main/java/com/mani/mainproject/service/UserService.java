package com.mani.mainproject.service;

import com.mani.mainproject.dto.ChangeRoleRequest;

public interface UserService {

    void changeUserRole(Long userId, ChangeRoleRequest request);
}
