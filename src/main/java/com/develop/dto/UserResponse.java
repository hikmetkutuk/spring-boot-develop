package com.develop.dto;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email
) {
}
