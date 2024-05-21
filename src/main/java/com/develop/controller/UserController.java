package com.develop.controller;

import com.develop.dto.UserRequest;
import com.develop.dto.UserResponse;
import com.develop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> addUser(@RequestBody UserRequest userRequest) {
        return userService.addUser(userRequest);
    }
}
