package com.klnsdr.axon;

import com.klnsdr.axon.auth.RequestRouteMatcher;
import org.springframework.stereotype.Component;

@Component
public class RestrictedRoutesConfig {
    public RequestRouteMatcher getRestrictedRoutes() {
        return RequestRouteMatcher.builder()
                .post("/api/shs/enroll/student")
                .post("/api/shs/enroll/teacher")
                .getWantsInfo("/api/users/menu")
                .build();
    }
}