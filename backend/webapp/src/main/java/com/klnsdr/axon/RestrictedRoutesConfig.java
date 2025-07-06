package com.klnsdr.axon;

import com.klnsdr.axon.auth.RequestRouteMatcher;
import org.springframework.stereotype.Component;

@Component
public class RestrictedRoutesConfig {
    public RequestRouteMatcher getRestrictedRoutes() {
        return RequestRouteMatcher.builder()
                .getWantsInfo("/api/users/menu")
                .build();
    }
}