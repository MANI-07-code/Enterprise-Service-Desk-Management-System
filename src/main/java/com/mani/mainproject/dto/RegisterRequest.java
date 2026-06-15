package com.mani.mainproject.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message= "Username is required")
    private String userName;

    @Email(message = "Invalid email formate")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 6,message = "Password must contain at least 6 characters")
    private String password;
}
