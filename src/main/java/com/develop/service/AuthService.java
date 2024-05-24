package com.develop.service;

import com.develop.model.Token;
import com.develop.model.TokenType;
import com.develop.model.User;
import com.develop.repository.TokenRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final TokenRepository tokenRepository;

    public AuthService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .type(TokenType.BEARER)
                .build();
        tokenRepository.save(token);
    }

    public void revokeAllUserTokens(User user) {
        var validToken = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validToken.isEmpty()) {
            return;
        }
        validToken.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validToken);
    }
}
