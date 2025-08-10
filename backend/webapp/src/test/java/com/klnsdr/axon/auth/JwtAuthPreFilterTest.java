package com.klnsdr.axon.auth;

import com.klnsdr.axon.RestrictedRoutesConfig;
import com.klnsdr.axon.auth.token.service.TokenService;
import com.klnsdr.axon.permissions.WellKnownPermissions;
import com.klnsdr.axon.permissions.service.PermissionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class JwtAuthPreFilterTest {
    private JwtUtil jwtUtil;
    private RequestRouteMatcher requestRouteMatcher;
    private TokenService tokenService;
    private PermissionService permissionService;
    private JwtAuthPreFilter filter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = mock(JwtUtil.class);
        requestRouteMatcher = mock(RequestRouteMatcher.class);
        tokenService = mock(TokenService.class);
        permissionService = mock(PermissionService.class);

        RestrictedRoutesConfig restrictedRoutesConfig = mock(RestrictedRoutesConfig.class);
        when(restrictedRoutesConfig.getRestrictedRoutes()).thenReturn(requestRouteMatcher);

        filter = new JwtAuthPreFilter(jwtUtil, restrictedRoutesConfig, tokenService, permissionService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void whenNoAuthorizationHeader_andRestrictedRoute_thenRespondUnauthorized() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/restricted");
        when(request.getMethod()).thenReturn("GET");
        when(requestRouteMatcher.isRestrictedRouteOptional("/restricted", "GET")).thenReturn(false);
        when(requestRouteMatcher.isRestrictedRoute("/restricted", "GET")).thenReturn(true);
        when(requestRouteMatcher.isRestrictedRouteNeedsPermission("/restricted", "GET")).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals("Unauthorized", responseWriter.toString());
        verify(response).getWriter();
        verifyNoInteractions(filterChain);
    }

    @Test
    void whenNoAuthorizationHeader_andNotRestrictedRoute_thenContinueFilterChain() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/public");
        when(request.getMethod()).thenReturn("GET");
        when(requestRouteMatcher.isRestrictedRouteOptional("/public", "GET")).thenReturn(false);
        when(requestRouteMatcher.isRestrictedRoute("/public", "GET")).thenReturn(false);
        when(requestRouteMatcher.isRestrictedRouteNeedsPermission("/public", "GET")).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(response, never()).setStatus(anyInt());
    }

    @Test
    void whenAuthorizationHeaderInvalidPrefix_andRouteRestricted_thenUnauthorized() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic abc123");
        when(request.getRequestURI()).thenReturn("/restricted");
        when(request.getMethod()).thenReturn("GET");
        when(requestRouteMatcher.isRestrictedRouteOptional("/restricted", "GET")).thenReturn(false);
        when(requestRouteMatcher.isRestrictedRoute("/restricted", "GET")).thenReturn(true);
        when(requestRouteMatcher.isRestrictedRouteNeedsPermission("/restricted", "GET")).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals("Unauthorized", responseWriter.toString());
        verifyNoInteractions(filterChain);
    }

    @Test
    void whenRouteIsOptional_andTokenInvalid_thenContinueFilterChain() throws Exception {
        String token = "token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getRequestURI()).thenReturn("/optional");
        when(request.getMethod()).thenReturn("GET");
        when(requestRouteMatcher.isRestrictedRouteOptional("/optional", "GET")).thenReturn(true);

        when(jwtUtil.isValidToken(token)).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(response, never()).setStatus(anyInt());
    }

    @Test
    void whenRouteNeedsAuthentication_andTokenValid_knownUserId_thenAuthenticateAndContinue() throws Exception {
        String token = "validtoken";
        Long userId = 42L;

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getRequestURI()).thenReturn("/auth");
        when(request.getMethod()).thenReturn("GET");

        when(requestRouteMatcher.isRestrictedRouteOptional("/auth", "GET")).thenReturn(false);
        when(requestRouteMatcher.isRestrictedRoute("/auth", "GET")).thenReturn(true);
        when(requestRouteMatcher.isRestrictedRouteNeedsPermission("/auth", "GET")).thenReturn(false);

        when(jwtUtil.isValidToken(token)).thenReturn(true);
        when(tokenService.isKnownToken(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn(userId);
        when(jwtUtil.validateToken(eq(token), any())).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(userId.toString(), SecurityContextHolder.getContext().getAuthentication().getName());

        verify(filterChain).doFilter(request, response);
        verify(response, never()).setStatus(anyInt());
    }

    @Test
    void whenRouteNeedsAuthentication_andTokenInvalid_thenUnauthorized() throws Exception {
        String token = "invalidtoken";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getRequestURI()).thenReturn("/auth");
        when(request.getMethod()).thenReturn("GET");

        when(requestRouteMatcher.isRestrictedRouteOptional("/auth", "GET")).thenReturn(false);
        when(requestRouteMatcher.isRestrictedRoute("/auth", "GET")).thenReturn(true);
        when(requestRouteMatcher.isRestrictedRouteNeedsPermission("/auth", "GET")).thenReturn(false);

        when(jwtUtil.isValidToken(token)).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertEquals("Unauthorized", responseWriter.toString());
        verifyNoInteractions(filterChain);
    }

    @Test
    void whenRouteNeedsPermission_andUserLacksPermissions_thenForbidden() throws Exception {
        String token = "token";
        Long userId = 10L;
        List<String> neededPermissions = List.of("PERM_X");

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getRequestURI()).thenReturn("/perm");
        when(request.getMethod()).thenReturn("GET");

        when(requestRouteMatcher.isRestrictedRouteOptional("/perm", "GET")).thenReturn(false);
        when(requestRouteMatcher.isRestrictedRoute("/perm", "GET")).thenReturn(false);
        when(requestRouteMatcher.isRestrictedRouteNeedsPermission("/perm", "GET")).thenReturn(true);
        when(requestRouteMatcher.getNeededPermissions("/perm", "GET")).thenReturn(neededPermissions);

        when(jwtUtil.isValidToken(token)).thenReturn(true);
        when(tokenService.isKnownToken(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn(userId);
        when(jwtUtil.validateToken(eq(token), any())).thenReturn(true);

        when(permissionService.hasPermission(userId, WellKnownPermissions.DEVELOPER.getName())).thenReturn(false);
        when(permissionService.hasPermission(userId, "PERM_X")).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        assertEquals("Forbidden", responseWriter.toString());
        verifyNoInteractions(filterChain);
    }

    @Test
    void whenRouteNeedsPermission_andUserHasPermission_thenAuthenticateAndContinue() throws Exception {
        String token = "token";
        Long userId = 10L;
        List<String> neededPermissions = List.of("PERM_X");

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getRequestURI()).thenReturn("/perm");
        when(request.getMethod()).thenReturn("GET");

        when(requestRouteMatcher.isRestrictedRouteOptional("/perm", "GET")).thenReturn(false);
        when(requestRouteMatcher.isRestrictedRoute("/perm", "GET")).thenReturn(false);
        when(requestRouteMatcher.isRestrictedRouteNeedsPermission("/perm", "GET")).thenReturn(true);
        when(requestRouteMatcher.getNeededPermissions("/perm", "GET")).thenReturn(neededPermissions);

        when(jwtUtil.isValidToken(token)).thenReturn(true);
        when(tokenService.isKnownToken(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn(userId);
        when(jwtUtil.validateToken(eq(token), any())).thenReturn(true);

        when(permissionService.hasPermission(userId, WellKnownPermissions.DEVELOPER.getName())).thenReturn(false);
        when(permissionService.hasPermission(userId, "PERM_X")).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(userId.toString(), SecurityContextHolder.getContext().getAuthentication().getName());

        verify(filterChain).doFilter(request, response);
        verify(response, never()).setStatus(anyInt());
    }
}
