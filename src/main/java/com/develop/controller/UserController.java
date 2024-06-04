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
                        .data(Map.of("user", response))
                        .message("User registered successfully")
                        .path("/api/v1/user/register")
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED)
                        .build()
        );
    }

    /**
     * A description of the entire Java function.
     *
     * @param request description of parameter
     * @return description of return value
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(userService.login(request));
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
     * Retrieves a list of all users. Requires ADMIN or SUPER_ADMIN role with
     * corresponding read authority.
     *
     * @return ResponseEntity containing a list of UserResponse objects
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN') and hasAnyAuthority('admin:read', 'super_admin:read')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
