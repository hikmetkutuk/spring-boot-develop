package com.develop.service;

import com.develop.dto.AuthRequest;
import com.develop.dto.AuthResponse;
import com.develop.dto.UserRequest;
import com.develop.dto.UserResponse;
import com.develop.exception.*;
import com.develop.model.User;
import com.develop.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthService authService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(UserRequest request) {
        try {
            if (userRepository.existsByEmail(request.email())) {
                throw new EmailAlreadyExistsException("Email already in use");
            }

            User newUser = User.builder()
                    .name(request.name())
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .role(request.role())
                    .build();
            var registeredUser = userRepository.save(newUser);
            var jwtToken = jwtService.generateToken(newUser);
            var refreshToken = jwtService.generateRefreshToken(newUser);
            authService.saveUserToken(registeredUser, jwtToken);
            log.info("User {} registered successfully", newUser.getEmail());
            return new AuthResponse(jwtToken, refreshToken);
        } catch (Exception e) {
            log.error("Failed to register user: {}", e.getMessage());
            throw new UserRegistrationException("Failed to register user: " + e.getMessage());
        }
    }

    public AuthResponse login(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
            var user = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new UserLoginException("User not found with email: " + request.email()));
            var token = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            authService.revokeAllUserTokens(user);
            authService.saveUserToken(user, token);
            log.info("User {} logged in successfully", user.getEmail());
            return new AuthResponse(token, refreshToken);
        } catch (Exception e) {
            log.error("Failed to login user: {}", e.getMessage());
            throw new UserLoginException("Failed to login user: " + e.getMessage());
        }
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                authService.revokeAllUserTokens(user);
                authService.saveUserToken(user, accessToken);
                var authResponse = new AuthResponse(accessToken, refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Cacheable(cacheNames = "userCache", cacheManager = "redisCacheManager")
    public List<UserResponse> getAllUsers() {
        try {
            var response = userRepository.findAll().stream()
                    .map(user -> new UserResponse(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getRole()
                    ))
                    .collect(Collectors.toList());
            log.info("Get all users successfully");
            return response;
        } catch (Exception e) {
            log.error("Failed to get all users: {}", e.getMessage());
            throw new UserRetrievalException("Failed to get all users: " + e.getMessage());
        }
    }

    @Cacheable(cacheNames = "userCache", cacheManager = "redisCacheManager")
    public UserResponse getUserById(Long id) {
        try {
            var user = userRepository.findById(id).orElseThrow();
            var response = new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole()
            );
            log.info("Get user with id {} successfully", id);
            return response;
        } catch (Exception e) {
            log.error("Failed to get user with id {}: {}", id, e.getMessage());
            throw new UserRetrievalException("Failed to get user with id " + id + ": " + e.getMessage());
        }
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "userCache", allEntries = true, cacheManager = "redisCacheManager")
            }
    )
    public UserResponse updateUser(Long id, UserRequest request) {
        try {
            var user = userRepository.findById(id).orElseThrow();
            user.setName(request.name());
            user.setEmail(request.email());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setRole(request.role());
            userRepository.save(user);
            log.info("User with id {} updated successfully", id);
            return new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole()
            );
        } catch (Exception e) {
            log.error("Failed to update user with id {}: {}", id, e.getMessage());
            throw new UserUpdateException("Failed to update user with id " + id + ": " + e.getMessage());
        }
    }
}
