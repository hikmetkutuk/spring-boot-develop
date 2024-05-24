package com.develop.dto;

public record AuthRequest(
        String email,
        String password
) {
}
