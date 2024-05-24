package com.develop.service;

import com.develop.dto.AuthRequest;
import com.develop.dto.AuthResponse;
import com.develop.dto.UserRequest;
import com.develop.exception.UserLoginException;
import com.develop.exception.UserRegistrationException;
import com.develop.model.Role;
import com.develop.model.User;
import com.develop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            User newUser = User.builder()
                    .name(request.name())
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .role(Role.USER)
                    .build();
            var registeredUser = userRepository.save(newUser);
            var jwtToken = jwtService.generateToken(newUser);
            authService.saveUserToken(registeredUser, jwtToken);
            log.info("User {} registered successfully", newUser.getEmail());
            return new AuthResponse(jwtToken);
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
            authService.revokeAllUserTokens(user);
            authService.saveUserToken(user, token);
            log.info("User {} logged in successfully", user.getEmail());
            return new AuthResponse(token);
        } catch (Exception e) {
            log.error("Failed to login user: {}", e.getMessage());
            throw new UserLoginException("Failed to login user: " + e.getMessage());
        }
    }
}
