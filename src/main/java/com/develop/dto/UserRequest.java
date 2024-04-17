package com.develop.dto;

import java.time.LocalDateTime;

public record UserRequest(
        String name,
        LocalDateTime birthDetails) {
}
