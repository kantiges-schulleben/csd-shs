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

                .deleteNeedsPermission("/api/users/*", WellKnownPermissions.DEVELOPER.getName())
                .getNeedsPermission("/api/users/*", WellKnownPermissions.DEVELOPER.getName())
                .getNeedsPermission("/api/users/search", WellKnownPermissions.DEVELOPER.getName())
                .getNeedsPermission("/api/users/", WellKnownPermissions.DEVELOPER.getName())
                .putNeedsPermission("/api/users/*", WellKnownPermissions.DEVELOPER.getName())

                .deleteNeedsPermission("/api/shs/admin/students/id/*", WellKnownPermissions.SHS_ADMIN.getName())
                .deleteNeedsPermission("/api/shs/admin/pair/id/*", WellKnownPermissions.SHS_ADMIN.getName())
                .getNeedsPermission("/api/shs/admin/teachers/by-subject", WellKnownPermissions.SHS_ADMIN.getName())
                .getNeedsPermission("/api/shs/admin/students/search", WellKnownPermissions.SHS_ADMIN.getName())
                .getNeedsPermission("/api/shs/admin/students/count", WellKnownPermissions.SHS_ADMIN.getName())
                .getNeedsPermission("/api/shs/admin/students/by-subject", WellKnownPermissions.SHS_ADMIN.getName())
                .getNeedsPermission("/api/shs/admin/pairs/without", WellKnownPermissions.SHS_ADMIN.getName())
                .getNeedsPermission("/api/shs/admin/pairs/single", WellKnownPermissions.SHS_ADMIN.getName())
                .getNeedsPermission("/api/shs/admin/pairs/group", WellKnownPermissions.SHS_ADMIN.getName())
                .getNeedsPermission("/api/shs/admin/is-phase-two", WellKnownPermissions.SHS_ADMIN.getName())
                .getNeedsPermission("/api/shs/admin/analysis/status", WellKnownPermissions.SHS_ADMIN.getName())
                .getNeedsPermission("/api/shs/admin/analysis/running", WellKnownPermissions.SHS_ADMIN.getName())
                .postNeedsPermission("/api/shs/admin/start", WellKnownPermissions.SHS_ADMIN.getName())
                .postNeedsPermission("/api/shs/admin/pairs", WellKnownPermissions.SHS_ADMIN.getName())
                .putNeedsPermission("/api/shs/admin/teachers/id/*", WellKnownPermissions.SHS_ADMIN.getName())
                .putNeedsPermission("/api/shs/admin/students/id/*", WellKnownPermissions.SHS_ADMIN.getName())
                .putNeedsPermission("/api/shs/admin/reset", WellKnownPermissions.SHS_ADMIN.getName())
                .putNeedsPermission("/api/shs/admin/pairs/id/*/release", WellKnownPermissions.SHS_ADMIN.getName())

                .getNeedsPermission("/api/permissions/user/*", WellKnownPermissions.DEVELOPER.getName())
                .getNeedsPermission("/api/permissions/", WellKnownPermissions.DEVELOPER.getName())
                .build();
    }
}