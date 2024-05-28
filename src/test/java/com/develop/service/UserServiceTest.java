package com.develop.service;

import com.develop.dto.AuthRequest;
import com.develop.dto.AuthResponse;
import com.develop.dto.UserRequest;
import com.develop.exception.UserLoginException;
import com.develop.exception.UserRegistrationException;
import com.develop.model.Role;
import com.develop.model.User;
import com.develop.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthService authService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private UserService userService;
    private UserRequest userRequest;
    private AuthRequest authRequest;
    private User user;
    private AutoCloseable closeable;


    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        userRequest = new UserRequest("John Doe", "john@example.com", "password123", Role.USER);
        authRequest = new AuthRequest("john@example.com", "password123");
        user = User.builder()
                .name(userRequest.name())
                .email(userRequest.email())
                .password("encodedPassword")
                .role(userRequest.role())
                .build();
    }

    @Test
    void register_SuccessfulRegistration_ReturnsAuthResponse() {
        String jwtToken = "mockJwtToken";
        String refreshToken = "mockRefreshToken";

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn(refreshToken);

        AuthResponse response = userService.register(userRequest);

        assertEquals(jwtToken, response.accessToken());
        assertEquals(refreshToken, response.refreshToken());
        verify(authService).saveUserToken(any(User.class), eq(jwtToken));
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
        verify(jwtService).generateRefreshToken(any(User.class));
    }

    @Test
    void register_UserRepositoryThrowsException_ThrowsUserRegistrationException() {
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        UserRegistrationException exception = assertThrows(UserRegistrationException.class, () -> userService.register(userRequest));

        assertEquals("Failed to register user: Database error", exception.getMessage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void login_SuccessfulLogin_ReturnsAuthResponse() {
        String jwtToken = "mockJwtToken";
        String refreshToken = "mockRefreshToken";

        when(userRepository.findByEmail(authRequest.email())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(user)).thenReturn(refreshToken);

        AuthResponse response = userService.login(authRequest);

        assertEquals(jwtToken, response.accessToken());
        assertEquals(refreshToken, response.refreshToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authService).revokeAllUserTokens(user);
        verify(authService).saveUserToken(user, jwtToken);
    }

    @Test
    void login_UserNotFound_ThrowsUserLoginException() {
        when(userRepository.findByEmail(authRequest.email())).thenReturn(Optional.empty());

        UserLoginException exception = assertThrows(UserLoginException.class, () -> userService.login(authRequest));

        assertEquals("Failed to login user: User not found with email: " + authRequest.email(), exception.getMessage());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_AuthenticationFails_ThrowsUserLoginException() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        UserLoginException exception = assertThrows(UserLoginException.class, () -> userService.login(authRequest));

        assertEquals("Failed to login user: Authentication failed", exception.getMessage());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
