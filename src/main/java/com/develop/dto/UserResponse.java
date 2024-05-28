package com.develop.dto;

import com.develop.model.Role;

public record UserResponse(
        Long id,
        String name,
        String email,
        Role role
) {
}
