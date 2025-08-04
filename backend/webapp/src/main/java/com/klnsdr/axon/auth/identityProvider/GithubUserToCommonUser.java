package com.klnsdr.axon.auth.identityProvider;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Objects;

public class GithubUserToCommonUser implements OAuthUserToCommonUser {
    private final OAuth2User oAuth2User;

    public GithubUserToCommonUser(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public String getId() {
        return Objects.requireNonNull(oAuth2User.getAttribute("id")).toString();
    }

    @Override
    public String getName() {
        return Objects.requireNonNull(oAuth2User.getAttribute("login"));
    }
}
