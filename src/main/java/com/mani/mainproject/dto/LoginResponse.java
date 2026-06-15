package com.mani.mainproject.dto;

import com.mani.mainproject.enums.Role;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private Long userId;
    private String userName;
    private String email;
    private Role role;

}
