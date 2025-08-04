package com.klnsdr.axon.auth;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class OAuthUserService {
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> run() {
        return userRequest -> {
            final String registrationId = userRequest.getClientRegistration().getRegistrationId();

            if ("nextcloud".equals(registrationId)) {
                return handleNextCloud(userRequest);
            }
            return new DefaultOAuth2UserService().loadUser(userRequest);
        };
    }

    private OAuth2User handleNextCloud(OAuth2UserRequest userRequest) {
            OAuth2AccessToken accessToken = userRequest.getAccessToken();
            ClientRegistration client = userRequest.getClientRegistration();

            final String userInfoEndpointUri = client.getProviderDetails().getUserInfoEndpoint().getUri();

            final RestTemplate restTemplate = new RestTemplate();
            final HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getTokenValue());

            final HttpEntity<Void> entity = new HttpEntity<>(headers);
            final ResponseEntity<Map> response = restTemplate.exchange(
                    userInfoEndpointUri,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            final Map<String, Object> userInfo = response.getBody();
            final Map<String, Object> ocs = (Map<String, Object>) userInfo.get("ocs");
            final Map<String, Object> data = (Map<String, Object>) ocs.get("data");

            return new DefaultOAuth2User(
                    List.of(new SimpleGrantedAuthority("ROLE_USER")),
                    data,
                    "id"
            );
    }
}
