package com.klnsdr.axon.auth;

import com.klnsdr.axon.RestrictedRoutesConfig;
import com.klnsdr.axon.auth.token.service.TokenService;
import com.klnsdr.axon.permissions.WellKnownPermissions;
import com.klnsdr.axon.permissions.service.PermissionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filter that handles JWT authentication for incoming HTTP requests.
 * This filter checks the validity of the JWT token and sets the authentication
 * in the security context if the token is valid.
 */
@Component
public class JwtAuthPreFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RequestRouteMatcher requestRouteMatcher;
    private final TokenService tokenService;
    private final PermissionService permissionService;

    /**
     * Constructs a new JwtAuthPreFilter with the specified dependencies.
     *
     * @param jwtUtil the utility class for handling JWT operations
     * @param restrictedRoutesConfig the configuration for restricted routes
     * @param tokenService the service for managing tokens
     */
    public JwtAuthPreFilter(JwtUtil jwtUtil, RestrictedRoutesConfig restrictedRoutesConfig, TokenService tokenService, PermissionService permissionService) {
        this.jwtUtil = jwtUtil;
        this.requestRouteMatcher = restrictedRoutesConfig.getRestrictedRoutes();
        this.tokenService = tokenService;
        this.permissionService = permissionService;
    }

    /**
     * Filters incoming requests and performs JWT authentication.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if an error occurs during filtering
     * @throws IOException if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String path = request.getRequestURI();
        final String method = request.getMethod();
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        final String token = authHeader.substring(7);

        if (requestRouteMatcher.isRestrictedRouteOptional(path, method)) {
            handleWantsAuthentication(token, request, response, filterChain);
            return;
        } else if (requestRouteMatcher.isRestrictedRoute(path, method)) {
            handleNeedsAuthentication(token, request, response, filterChain);
            return;
        } else if (requestRouteMatcher.isRestrictedRouteNeedsPermission(path, method)) {
            handleNeedsPermission(token, request, response, filterChain);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void handleNeedsAuthentication(String token, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!jwtUtil.isValidToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        if (!tokenService.isKnownToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        final Long userId = jwtUtil.extractUserId(token);

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        final UserDetails userDetails = User.builder()
                .username(userId.toString())
                .password("")
                .build();

        if (!jwtUtil.validateToken(token, userDetails)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private void handleWantsAuthentication(String token, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!jwtUtil.isValidToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!tokenService.isKnownToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        final Long userId = jwtUtil.extractUserId(token);

        if (userId == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        final UserDetails userDetails = User.builder()
                .username(userId.toString())
                .password("")
                .build();

        if (!jwtUtil.validateToken(token, userDetails)) {
            filterChain.doFilter(request, response);
            return;
        }

        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private void handleNeedsPermission(String token, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!jwtUtil.isValidToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        if (!tokenService.isKnownToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        final Long userId = jwtUtil.extractUserId(token);

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        final UserDetails userDetails = User.builder()
                .username(userId.toString())
                .password("")
                .build();

        if (!jwtUtil.validateToken(token, userDetails)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        final List<String> allowedPermissions = requestRouteMatcher.getNeededPermissions(request.getRequestURI(), request.getMethod());
        if (allowedPermissions == null || allowedPermissions.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return;
        }

        if (!permissionService.hasPermission(userId, WellKnownPermissions.DEVELOPER.getName())) {
            final boolean hasPermission = allowedPermissions.stream()
                    .anyMatch(permission -> permissionService.hasPermission(userId, permission));

            if (!hasPermission) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Forbidden");
                return;
            }
        }

        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}