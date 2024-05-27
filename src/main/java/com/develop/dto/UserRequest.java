package com.develop.dto;

import com.develop.model.Role;

public record UserRequest(
        String name,
        String email,
        String password,
        Role role
) {
}
