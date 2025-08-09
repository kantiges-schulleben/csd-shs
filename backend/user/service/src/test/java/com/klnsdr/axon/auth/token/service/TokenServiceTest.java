package com.klnsdr.axon.auth.token.service;

import com.klnsdr.axon.auth.token.entity.TokenEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenServiceTest {
    private TokenService tokenService;
    private TokenRepository tokenRepository;

    @BeforeEach
    public void setUp() {
        tokenRepository = mock(TokenRepository.class);
        tokenService = new TokenService(tokenRepository);
    }

    @Test
    public void testCreateToken() {
        when(tokenRepository.save(any(TokenEntity.class)))
            .thenReturn(new TokenEntity("testToken", new Date(System.currentTimeMillis() + 3600000)));

        final TokenEntity tokenEntity = tokenService.save("testToken", new Date(System.currentTimeMillis() + 3600000));
        assertNotNull(tokenEntity);
    }

    @Test
    public void testIsKnownToken() {
        when(tokenRepository.existsById("knownToken")).thenReturn(true);

        boolean isKnown = tokenService.isKnownToken("knownToken");
        assertTrue(isKnown);
    }

    @Test
    public void testIsNotKnownToken() {
        when(tokenRepository.existsById("unknownToken")).thenReturn(false);

        boolean isKnown = tokenService.isKnownToken("unknownToken");
        assertFalse(isKnown);
    }

    @Test
    public void testRemoveToken() {
        final TokenEntity tokenEntity = new TokenEntity("removableToken", new Date(System.currentTimeMillis() + 3600000));
        when(tokenRepository.findById("removableToken")).thenReturn(Optional.of(tokenEntity));
        when(tokenRepository.save(any(TokenEntity.class))).thenReturn(tokenEntity);

        final TokenEntity removedToken = tokenService.removeToken("removableToken");
        assertNotNull(removedToken);
        assertEquals(tokenEntity, removedToken);
    }

    @Test
    public void testRemoveNonExistentToken() {
        when(tokenRepository.findById("nonExistentToken")).thenReturn(Optional.empty());

        final TokenEntity removedToken = tokenService.removeToken("nonExistentToken");
        assertNull(removedToken);
    }
}
