package com.klnsdr.axon.auth;

import com.klnsdr.axon.auth.identityProvider.GithubUserToCommonUser;
import com.klnsdr.axon.auth.identityProvider.NextCloudUserToCommonUser;
import com.klnsdr.axon.auth.identityProvider.OAuthUserToCommonUser;
import com.klnsdr.axon.auth.token.service.TokenService;
import com.klnsdr.axon.user.entity.UserEntity;
import com.klnsdr.axon.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * Handler for successful OAuth2 authentication.
 * This class implements the AuthenticationSuccessHandler interface and handles
 * the actions to be taken upon successful authentication.
 */
@Component
public class OAuthHandler implements AuthenticationSuccessHandler {
    private final Logger logger = LoggerFactory.getLogger(OAuthHandler.class);
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final TokenService tokenService;

    /**
     * The URL to redirect to after successful authentication.
     */
    @Value("${app.oauth2.successRedirectUrl}")
    private String redirectUrl = "https://example.com/welcome";

    public OAuthHandler(JwtUtil jwtUtil, UserService userService, TokenService tokenService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.tokenService = tokenService;
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
            final OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            final String authClient = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
            final OAuthUserToCommonUser oAuthUserProviderWrapper = getOAuthUserProviderWrapper(authClient, oAuth2User);

            if (oAuth2User == null) {
                response.sendRedirect("/login?error=true");
                return;
            }
            final String idpID = oAuthUserProviderWrapper.getId();

            final Optional<UserEntity> userOptional = userService.findByIdpID(idpID);
            UserEntity user;
            if (userOptional.isPresent()) {
                user = userOptional.get();
            } else {
                logger.debug("Creating new user with IDP ID: {}", idpID);
                final String name = oAuthUserProviderWrapper.getName();
                user = userService.createUser(idpID, name);
            }

            final String token = jwtUtil.generateToken(user, oAuthUserProviderWrapper);
            final Date validTill = jwtUtil.extractExpiration(token);

            tokenService.save(token, validTill);

            response.sendRedirect(redirectUrl + "?token=" + token);
        } catch (Exception e) {
            logger.error("Error during OAuth2 authentication success handling", e);
            response.sendRedirect("/login?error=true");
        }
    }

    OAuthUserToCommonUser getOAuthUserProviderWrapper(String authClient, OAuth2User oAuth2User) {
        if ("github".equals(authClient)) {
            return new GithubUserToCommonUser(oAuth2User);
        } else if ("nextcloud".equals(authClient)) {
            return new NextCloudUserToCommonUser(oAuth2User);
        }
        throw new IllegalArgumentException("Unsupported OAuth2 provider: " + authClient);
    }
}
