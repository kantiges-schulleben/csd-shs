package com.klnsdr.axon.auth;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Payload;
import com.klnsdr.axon.auth.identityProvider.OAuthUserToCommonUser;
import com.klnsdr.axon.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtUtilTest {
    private JwtUtil jwtUtil;
    private OAuthUserToCommonUser oAuthUserToCommonUser;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        oAuthUserToCommonUser = mock(OAuthUserToCommonUser.class);
        when(oAuthUserToCommonUser.getId()).thenReturn("oauth-user-id");
        when(oAuthUserToCommonUser.getName()).thenReturn("oauth-username");

        userEntity = new UserEntity();
        userEntity.setId(1L);
    }

    @Test
    void generateToken_and_extractClaims() {
        String token = jwtUtil.generateToken(userEntity, oAuthUserToCommonUser);
        assertNotNull(token);

        Long extractedUserId = jwtUtil.extractUserId(token);
        assertEquals(userEntity.getId(), extractedUserId);

        String subject = jwtUtil.extractSubject(token);
        assertEquals("oauth-user-id", subject);

        Date expiration = jwtUtil.extractExpiration(token);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void isValidToken_returnsTrue_forValidToken() {
        String token = jwtUtil.generateToken(userEntity, oAuthUserToCommonUser);
        assertTrue(jwtUtil.isValidToken(token));
    }

    @Test
    void isValidToken_returnsFalse_forInvalidToken() {
        String invalidToken = "this.is.not.a.valid.token";
        assertFalse(jwtUtil.isValidToken(invalidToken));
    }

    @Test
    void extractClaim_returnsCorrectClaim() {
        String token = jwtUtil.generateToken(userEntity, oAuthUserToCommonUser);

        String usernameClaim = jwtUtil.extractClaim(token, jwt -> jwt.getClaim("username").asString());
        assertEquals("oauth-username", usernameClaim);
    }

    @Test
    void validateToken_returnsTrue_forValidTokenAndUserDetails() {
        String token = jwtUtil.generateToken(userEntity, oAuthUserToCommonUser);

        var userDetails = User.withUsername(userEntity.getId().toString())
                .password("irrelevant")
                .authorities("ROLE_USER")
                .build();

        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void validateToken_returnsFalse_forMismatchedUserDetails() {
        String token = jwtUtil.generateToken(userEntity, oAuthUserToCommonUser);

        var userDetails = User.withUsername("some-other-id")
                .password("irrelevant")
                .authorities("ROLE_USER")
                .build();

        assertFalse(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void validateToken_returnsFalse_forExpiredToken() throws InterruptedException {
        jwtUtil.jwtTokenValidity = 100;
        String token = jwtUtil.generateToken(userEntity, oAuthUserToCommonUser);

        Thread.sleep(200);

        var userDetails = User.withUsername(userEntity.getId().toString())
                .password("irrelevant")
                .authorities("ROLE_USER")
                .build();


        RuntimeException ex = assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(token, userDetails));

        assertInstanceOf(TokenExpiredException.class, ex.getCause());
    }

    @Test
    void extractAllClaims_throwsRuntimeException_forInvalidToken() {
        String invalidToken = "invalid.token.value";

        RuntimeException ex = assertThrows(RuntimeException.class, () -> jwtUtil.extractClaim(invalidToken, Payload::getSubject));

        assertInstanceOf(JWTVerificationException.class, ex.getCause());
    }
}
