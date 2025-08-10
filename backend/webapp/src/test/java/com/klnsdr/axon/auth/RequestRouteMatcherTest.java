package com.klnsdr.axon.auth;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RequestRouteMatcherTest {
    @Test
    void testIsRestrictedRoute_withExactMatch() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder()
                .get("/api/resource")
                .post("/api/resource")
                .put("/api/resource")
                .delete("/api/resource")
                .build();

        assertTrue(matcher.isRestrictedRoute("/api/resource", "GET"));
        assertTrue(matcher.isRestrictedRoute("/api/resource", "POST"));
        assertTrue(matcher.isRestrictedRoute("/api/resource", "PUT"));
        assertTrue(matcher.isRestrictedRoute("/api/resource", "DELETE"));
        assertFalse(matcher.isRestrictedRoute("/api/other", "GET"));
    }

    @Test
    void testIsRestrictedRoute_withWildcards() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder()
                .get("/api/resource/*")
                .post("/api/resource/*")
                .build();

        assertTrue(matcher.isRestrictedRoute("/api/resource/123", "GET"));
        assertTrue(matcher.isRestrictedRoute("/api/resource/abc", "POST"));

        assertFalse(matcher.isRestrictedRoute("/api/resource/", "GET"));

        assertFalse(matcher.isRestrictedRoute("/api/resource/123", "PUT"));
    }

    @Test
    void testIsRestrictedRouteOptional() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder()
                .getWantsInfo("/optional/get/*")
                .postWantsInfo("/optional/post/*")
                .build();

        assertTrue(matcher.isRestrictedRouteOptional("/optional/get/42", "GET"));
        assertTrue(matcher.isRestrictedRouteOptional("/optional/post/abc", "POST"));

        assertTrue(matcher.isRestrictedRoute("/optional/get/42", "GET"));
        assertFalse(matcher.isRestrictedRouteOptional("/optional/put/1", "PUT"));
    }

    @Test
    void testIsRestrictedRouteNeedsPermission_andGetNeededPermissions() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder()
                .getNeedsPermission("/perm/get/*", "read")
                .postNeedsPermission("/perm/post/*", "write")
                .build();

        assertTrue(matcher.isRestrictedRouteNeedsPermission("/perm/get/123", "GET"));
        assertEquals(List.of("read"), matcher.getNeededPermissions("/perm/get/123", "GET"));

        assertTrue(matcher.isRestrictedRouteNeedsPermission("/perm/post/xyz", "POST"));
        assertEquals(List.of("write"), matcher.getNeededPermissions("/perm/post/xyz", "POST"));

        assertFalse(matcher.isRestrictedRouteNeedsPermission("/perm/put/abc", "PUT"));
        assertTrue(matcher.getNeededPermissions("/perm/put/abc", "PUT").isEmpty());
    }

    @Test
    void testGetNeededPermissions_returnsEmptyForNoMatch() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder()
                .getNeedsPermission("/perm/get/*", "read")
                .build();

        assertTrue(matcher.getNeededPermissions("/other/path", "GET").isEmpty());

        assertTrue(matcher.getNeededPermissions("/perm/get/123", "PATCH").isEmpty());
    }

    @Test
    void testBuilderReplacesWildcardsCorrectly() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder()
                .get("/foo/*/bar")
                .postNeedsPermission("/foo/*/baz", "perm1")
                .build();

        assertTrue(matcher.isRestrictedRoute("/foo/123/bar", "GET"));
        assertFalse(matcher.isRestrictedRoute("/foo/123/456/bar", "GET"));

        assertTrue(matcher.isRestrictedRouteNeedsPermission("/foo/abc/baz", "POST"));
        assertEquals(List.of("perm1"), matcher.getNeededPermissions("/foo/abc/baz", "POST"));
    }

    @Test
    void testCaseInsensitiveMethodHandling() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder()
                .get("/path")
                .build();

        assertTrue(matcher.isRestrictedRoute("/path", "get"));
        assertTrue(matcher.isRestrictedRoute("/path", "GET"));
        assertFalse(matcher.isRestrictedRoute("/path", "post"));
    }

    @Test
    void testUnknownMethodReturnsFalse() {
        RequestRouteMatcher matcher = RequestRouteMatcher.builder()
                .get("/path")
                .build();

        assertFalse(matcher.isRestrictedRoute("/path", "PATCH"));
        assertFalse(matcher.isRestrictedRouteOptional("/path", "PATCH"));
        assertFalse(matcher.isRestrictedRouteNeedsPermission("/path", "PATCH"));
        assertTrue(matcher.getNeededPermissions("/path", "PATCH").isEmpty());
    }
}
