package com.develop.dto;

public record UserRequest(
        String name,
        String email,
        String password) {
}
