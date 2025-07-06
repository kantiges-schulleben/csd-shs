package com.klnsdr.axon.auth.token.service;

import com.klnsdr.axon.auth.token.entity.TokenEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public TokenEntity save(String token, Date validTill) {
        return tokenRepository.save(new TokenEntity(token, validTill));
    }

    public boolean isKnownToken(String token) {
        return tokenRepository.existsById(token);
    }

    public TokenEntity removeToken(String token) {
        Optional<TokenEntity> tokenEntity = tokenRepository.findById(token);
        if (tokenEntity.isEmpty()) {
            return null;
        }
        tokenRepository.delete(tokenEntity.get());
        return tokenEntity.get();
    }
}
