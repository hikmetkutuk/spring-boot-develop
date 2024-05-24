package com.develop.service;

import com.develop.dto.AuthRequest;
import com.develop.dto.AuthResponse;
import com.develop.dto.UserRequest;
import com.develop.exception.UserLoginException;
import com.develop.exception.UserRegistrationException;
import com.develop.model.Role;
import com.develop.model.Token;
import com.develop.model.TokenType;
import com.develop.model.User;
import com.develop.repository.TokenRepository;
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
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
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
            saveUserToken(registeredUser, jwtToken);
            log.info("User {} registered successfully", newUser.getEmail());
            return new AuthResponse(jwtToken);
        } catch (Exception e) {
            log.error("Failed to register user: {}", e.getMessage());
            throw new UserRegistrationException("Failed to register user: " + e.getMessage());
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .type(TokenType.BEARER)
                .build();
        tokenRepository.save(token);
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
            saveUserToken(user, token);
            log.info("User {} logged in successfully", user.getEmail());
            return new AuthResponse(token);
        } catch (Exception e) {
            log.error("Failed to login user: {}", e.getMessage());
            throw new UserLoginException("Failed to login user: " + e.getMessage());
        }
    }
}
