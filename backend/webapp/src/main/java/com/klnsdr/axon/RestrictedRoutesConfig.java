package com.klnsdr.axon;

import com.klnsdr.axon.auth.RequestRouteMatcher;
import com.klnsdr.axon.permissions.WellKnownPermissions;
import org.springframework.stereotype.Component;

@Component
public class RestrictedRoutesConfig {
    public RequestRouteMatcher getRestrictedRoutes() {
        return RequestRouteMatcher.builder()
                .post("/api/shs/enroll/student")
                .post("/api/shs/enroll/teacher")
                .getWantsInfo("/api/users/menu")
                .getNeedsPermission("/api/shs/admin/students/count", WellKnownPermissions.SHS_ADMIN.getName())
                .getNeedsPermission("/api/shs/admin/students/search", WellKnownPermissions.SHS_ADMIN.getName())
                .getNeedsPermission("/api/shs/admin/students/id/*", WellKnownPermissions.SHS_ADMIN.getName())
                .putNeedsPermission("/api/shs/admin/students/id/*", WellKnownPermissions.SHS_ADMIN.getName())
                .deleteNeedsPermission("/api/shs/admin/students/id/*", WellKnownPermissions.SHS_ADMIN.getName())
                .postNeedsPermission("/api/shs/admin/start", WellKnownPermissions.SHS_ADMIN.getName())
                .getNeedsPermission("/api/shs/admin/analysis/status", WellKnownPermissions.SHS_ADMIN.getName())
                .getNeedsPermission("/api/shs/admin/analysis/running", WellKnownPermissions.SHS_ADMIN.getName())
                .build();
    }
}