package com.develop.controller;

import com.develop.dto.*;
import com.develop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user based on the provided UserRequest.
     *
     * @param request the UserRequest object containing user registration details
     * @return an HTTP response with status code, message, timestamp, and user data
     */
    @PostMapping("/register")
    public ResponseEntity<HttpResponse> register(@Valid @RequestBody UserRequest request) {
        AuthResponse response = userService.register(request);
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of("response", response))
                        .message("User registered successfully")
                        .path("/api/v1/user/register")
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED)
                        .build()
        );
    }

    /**
     * Generates a response entity for user login.
     *
     * @param request the authentication request
     * @return response entity with login information
     */
    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of("response", response))
                        .message("User logged in successfully")
                        .path("/api/v1/user/login")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    /**
     * Refreshes the token by calling the userService method.
     *
     * @param request  the HttpServletRequest object
     * @param response the HttpServletResponse object
     */
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.refreshToken(request, response);
    }

    /**
     * Get all users with roles ADMIN or SUPER_ADMIN and authorities admin:read or super_admin:read.
     *
     * @return Response entity containing a list of users and success message
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN') and hasAnyAuthority('admin:read', 'super_admin:read')")
    public ResponseEntity<HttpResponse> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of("users", users))
                        .message("Users retrieved successfully")
                        .path("/api/v1/user/list")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .build()
        );
    }
}
