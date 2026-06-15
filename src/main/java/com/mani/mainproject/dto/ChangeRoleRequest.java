package com.mani.mainproject.dto;

import com.mani.mainproject.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRoleRequest {

    private Role role;
}
