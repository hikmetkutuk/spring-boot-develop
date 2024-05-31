package com.develop.dto;

import com.develop.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "Name is mandatory")
        @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
        String name,
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email should be valid")
        String email,
        @NotBlank(message = "Password is mandatory")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password,
        @NotNull(message = "Role is mandatory")
        Role role
) {
}
