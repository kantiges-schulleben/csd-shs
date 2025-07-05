package com.klnsdr.axon.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * Handler for successful OAuth2 authentication.
 * This class implements the AuthenticationSuccessHandler interface and handles
 * the actions to be taken upon successful authentication.
 */
@Component
public class OAuthHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    /**
     * The URL to redirect to after successful authentication.
     */
    @Value("${app.oauth2.successRedirectUrl}")
    private String redirectUrl;

    public OAuthHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Handles successful authentication by creating a new user in the system if it does not exist
     * and redirecting to the configured URL.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param authentication the authentication object
     * @throws IOException if an input or output exception occurs
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            final String token = jwtUtil.generateToken(oAuth2User);
            final Date validTill = jwtUtil.extractExpiration(token);

            System.out.println(token);

            // Redirect to the desired URL after login
            response.sendRedirect(redirectUrl + "?token=" + token);
        } catch (Exception e) {
            response.sendRedirect("/login?error=true");
        }
    }
}
