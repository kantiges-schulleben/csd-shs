package com.klnsdr.axon.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for handling JWT operations such as generating and validating tokens.
 */
@Component
public class JwtUtil {
    @Value("${app.auth.jwt.secret}")
    private String SECRET = "secret";
    @Value("${app.auth.jwt.expiration}")
    long jwtTokenValidity = 3600000;

    /**
     * Generates a JWT token for the given OAuth2 user and internal user.
     *
     * @param oAuth2User the OAuth2 user
     * @return the generated JWT token
     */
    public String generateToken(OAuth2User oAuth2User) {
        return JWT.create()
                .withSubject(oAuth2User.getName())
                .withClaim("id", 1)
                .withClaim("username", (String) oAuth2User.getAttribute("login"))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtTokenValidity))
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * Checks if the given JWT token is valid.
     *
     * @param token the JWT token
     * @return true if the token is valid, false otherwise
     */
    public boolean isValidToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token
     * @return the username
     */
    public String extractUsername(String token) {
        return extractClaim(token, jwt -> jwt.getClaim("username").asString());
    }

    /**
     * Extracts the subject from the given JWT token.
     *
     * @param token the JWT token
     * @return the subject
     */
    public String extractSubject(String token) {
        return extractClaim(token, DecodedJWT::getSubject);
    }

    /**
     * Extracts a specific claim from the given JWT token.
     *
     * @param token the JWT token
     * @param claimsResolver the function to resolve the claim
     * @param <T> the type of the claim
     * @return the claim
     */
    public <T> T extractClaim(String token, Function<DecodedJWT, T> claimsResolver) {
        final DecodedJWT decodedJWT = extractAllClaims(token);
        return claimsResolver.apply(decodedJWT);
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token the JWT token
     * @return the decoded JWT
     */
    private DecodedJWT extractAllClaims(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /**
     * Validates the given JWT token against the provided user details.
     *
     * @param token the JWT token
     * @param userDetails the user details
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String subject = extractSubject(token);
        return (subject.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if the given JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token the JWT token
     * @return the expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, DecodedJWT::getExpiresAt);
    }
}