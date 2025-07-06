package com.klnsdr.axon.auth.token.service;

import com.klnsdr.axon.auth.token.entity.TokenEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

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
}
