package com.develop.controller;

import com.develop.dto.AuthRequest;
import com.develop.dto.AuthResponse;
import com.develop.dto.UserRequest;
import com.develop.dto.UserResponse;
import com.develop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
