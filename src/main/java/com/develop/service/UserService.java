package com.develop.service;

import com.develop.dto.UserRequest;
import com.develop.dto.UserResponse;
import com.develop.model.User;
import com.develop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<UserResponse> addUser(UserRequest userRequest) {
        try {
            User newUser = User.builder()
                    .name(userRequest.name())
                    .birthDetails(userRequest.birthDetails())
                    .build();
            userRepository.save(newUser);
            log.info("User added successfully: {}", userRequest.name());
            UserResponse userResponse = new UserResponse(newUser.getId(), newUser.getName(), newUser.getBirthDetails());
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        } catch (DataAccessException e) {
            log.error("An error occurred while adding a new user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
