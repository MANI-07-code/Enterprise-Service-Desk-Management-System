package com.mani.mainproject.dto;

import com.mani.mainproject.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeStatusRequest {
    private UserStatus status;

}
