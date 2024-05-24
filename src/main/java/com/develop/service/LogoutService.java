package com.develop.service;

import com.develop.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;
    private static final Logger log = LoggerFactory.getLogger(LogoutService.class);

    public LogoutService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        try {
            jwt = authHeader.substring(7);
            var storeToken = tokenRepository.findByToken(jwt)
                    .orElseThrow(() -> new RuntimeException("Token not found: " + jwt));
            storeToken.setExpired(true);
            storeToken.setRevoked(true);
            tokenRepository.save(storeToken);
            log.info("User logged out successfully with token: {}", jwt);
        } catch (Exception e) {
            log.error("Failed to logout: {}", e.getMessage());
        }
    }
}
