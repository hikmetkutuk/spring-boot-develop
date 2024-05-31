package com.develop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email should be valid")
        String email,
        @NotBlank(message = "Password is mandatory")
        String password
) {
}
