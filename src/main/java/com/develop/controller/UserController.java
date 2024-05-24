package com.develop.controller;

import com.develop.dto.AuthRequest;
import com.develop.dto.AuthResponse;
import com.develop.dto.UserRequest;
import com.develop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a user based on the provided UserRequest.
     *
     * @param request the UserRequest containing user information
     * @return ResponseEntity with AuthResponse containing registration status
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    /**
     * A description of the entire Java function.
     *
     * @param request description of parameter
     * @return description of return value
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
