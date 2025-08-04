package com.klnsdr.axon.auth.identityProvider;

import org.springframework.security.oauth2.core.user.OAuth2User;

public class NextCloudUserToCommonUser implements OAuthUserToCommonUser {
    private final OAuth2User oAuth2User;

    public NextCloudUserToCommonUser(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public String getId() {
        return oAuth2User.getAttribute("id").toString();
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("displayname").toString();
    }
}
