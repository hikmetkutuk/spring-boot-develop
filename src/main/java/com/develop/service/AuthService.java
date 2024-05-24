package com.develop.service;

import com.develop.model.Token;
import com.develop.model.TokenType;
import com.develop.model.User;
import com.develop.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {
    private final TokenRepository tokenRepository;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void saveUserToken(User user, String jwtToken) {
        try {
            var token = Token.builder()
                    .user(user)
                    .token(jwtToken)
                    .expired(false)
                    .revoked(false)
                    .type(TokenType.BEARER)
                    .build();
            tokenRepository.save(token);
            log.info("Token saved successfully for user: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to save token for user: {}", user.getEmail(), e);
            throw new RuntimeException("Failed to save token for user: " + user.getEmail(), e);
        }
    }

    public void revokeAllUserTokens(User user) {
        try {
            var validTokens = tokenRepository.findAllValidTokensByUser(user.getId());
            if (validTokens.isEmpty()) {
                log.info("No valid tokens found for user: {}", user.getEmail());
                return;
            }
            validTokens.forEach(t -> {
                t.setExpired(true);
                t.setRevoked(true);
            });
            tokenRepository.saveAll(validTokens);
            log.info("All tokens revoked successfully for user: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to revoke tokens for user: {}", user.getEmail(), e);
            throw new RuntimeException("Failed to revoke tokens for user: " + user.getEmail(), e);
        }
    }
}
