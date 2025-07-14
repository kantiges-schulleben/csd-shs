package com.klnsdr.axon.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for matching HTTP request routes to determine if they are restricted.
 */
public class RequestRouteMatcher {
    private final List<String> getRoutes;
    private final List<String> postRoutes;
    private final List<String> putRoutes;
    private final List<String> deleteRoutes;
    private final List<String> getRoutesOptional;
    private final List<String> postRoutesOptional;
    private final List<String> putRoutesOptional;
    private final List<String> deleteRoutesOptional;
    private final Map<String, List<String>> getRoutesNeedsPermission;
    private final Map<String, List<String>> postRoutesNeedsPermission;
    private final Map<String, List<String>> putRoutesNeedsPermission;
    private final Map<String, List<String>> deleteRoutesNeedsPermission;

    /**
     * Constructs a new RequestRouteMatcher with the specified routes for each HTTP method.
     *
     * @param getRoutes the list of GET routes
     * @param postRoutes the list of POST routes
     * @param putRoutes the list of PUT routes
     * @param deleteRoutes the list of DELETE routes
     */
    public RequestRouteMatcher(
            List<String> getRoutes,
            List<String> postRoutes,
            List<String> putRoutes,
            List<String> deleteRoutes,
            List<String> getRoutesOptional,
            List<String> postRoutesOptional,
            List<String> putRoutesOptional,
            List<String> deleteRoutesOptional,
            Map<String, List<String>> getRoutesNeedsPermission,
            Map<String, List<String>> postRoutesNeedsPermission,
            Map<String, List<String>> putRoutesNeedsPermission,
            Map<String, List<String>> deleteRoutesNeedsPermission
    ) {
        this.getRoutes = getRoutes;
        this.postRoutes = postRoutes;
        this.putRoutes = putRoutes;
        this.deleteRoutes = deleteRoutes;
        this.getRoutesOptional = getRoutesOptional;
        this.postRoutesOptional = postRoutesOptional;
        this.putRoutesOptional = putRoutesOptional;
        this.deleteRoutesOptional = deleteRoutesOptional;
        this.getRoutesNeedsPermission = getRoutesNeedsPermission;
        this.postRoutesNeedsPermission = postRoutesNeedsPermission;
        this.putRoutesNeedsPermission = putRoutesNeedsPermission;
        this.deleteRoutesNeedsPermission = deleteRoutesNeedsPermission;
    }

    /**
     * Checks if the given path and method correspond to a restricted route.
     *
     * @param path the request path
     * @param method the HTTP method
     * @return true if the route is restricted, false otherwise
     */
    public boolean isRestrictedRoute(String path, String method) {
        return switch (method.toUpperCase()) {
            case "GET" -> getRoutes.stream().anyMatch(path::matches);
            case "POST" -> postRoutes.stream().anyMatch(path::matches);
            case "PUT" -> putRoutes.stream().anyMatch(path::matches);
            case "DELETE" -> deleteRoutes.stream().anyMatch(path::matches);
            default -> false;
        };
    }

    public boolean isRestrictedRouteOptional(String path, String method) {
        return switch (method.toUpperCase()) {
            case "GET" -> getRoutesOptional.stream().anyMatch(path::matches);
            case "POST" -> postRoutesOptional.stream().anyMatch(path::matches);
            case "PUT" -> putRoutesOptional.stream().anyMatch(path::matches);
            case "DELETE" -> deleteRoutesOptional.stream().anyMatch(path::matches);
            default -> false;
        };
    }

    public boolean isRestrictedRouteNeedsPermission(String path, String method) {
        return !getNeededPermissions(path, method).isEmpty();
    }

    public List<String> getNeededPermissions(String path, String method) {
        return switch (method.toUpperCase()) {
            case "GET" -> {
                final List<String> keys = getRoutesNeedsPermission.keySet().stream()
                        .filter(path::matches)
                        .toList();
                if (keys.isEmpty()) {
                    yield List.of();
                }
                yield getRoutesNeedsPermission.get(keys.getFirst());
            }
            case "POST" -> {
                final List<String> keys = postRoutesNeedsPermission.keySet().stream()
                        .filter(path::matches)
                        .toList();
                if (keys.isEmpty()) {
                    yield List.of();
                }
                yield postRoutesNeedsPermission.get(keys.getFirst());
            }
            case "PUT" -> {
                final List<String> keys = putRoutesNeedsPermission.keySet().stream()
                        .filter(path::matches)
                        .toList();
                if (keys.isEmpty()) {
                    yield List.of();
                }
                yield putRoutesNeedsPermission.get(keys.getFirst());
            }
            case "DELETE" -> {
                final List<String> keys = deleteRoutesNeedsPermission.keySet().stream()
                        .filter(path::matches)
                        .toList();
                if (keys.isEmpty()) {
                    yield List.of();
                }
                yield deleteRoutesNeedsPermission.get(keys.getFirst());
            }
            default -> List.of();
        };
    }

    /**
     * Creates a new builder for constructing a RequestRouteMatcher.
     *
     * @return a new RestrictedRoutesBuilder
     */
    public static RestrictedRoutesBuilder builder() {
        return new RestrictedRoutesBuilder();
    }

    /**
     * Builder class for constructing a RequestRouteMatcher with specified routes.
     */
    public static class RestrictedRoutesBuilder {
        private static final String PATTERN_STAR = "[^/]+";
        private final List<String> getRoutes = new ArrayList<>();
        private final List<String> postRoutes = new ArrayList<>();
        private final List<String> putRoutes = new ArrayList<>();
        private final List<String> deleteRoutes = new ArrayList<>();
        private final List<String> getRoutesOptional = new ArrayList<>();
        private final List<String> postRoutesOptional = new ArrayList<>();
        private final List<String> putRoutesOptional = new ArrayList<>();
        private final List<String> deleteRoutesOptional = new ArrayList<>();
        private final Map<String, List<String>> getRoutesNeedsPermission = new HashMap<>();
        private final Map<String, List<String>> postRoutesNeedsPermission = new HashMap<>();
        private final Map<String, List<String>> putRoutesNeedsPermission = new HashMap<>();
        private final Map<String, List<String>> deleteRoutesNeedsPermission = new HashMap<>();

        /**
         * Adds a GET route to the builder.
         *
         * @param path the GET route
         * @return the builder instance
         */
        public RestrictedRoutesBuilder get(String path) {
            getRoutes.add(path);
            return this;
        }

        public RestrictedRoutesBuilder getWantsInfo(String path) {
            getRoutesOptional.add(path);
            get(path);
            return this;
        }

        public RestrictedRoutesBuilder getNeedsPermission(String path, String permission) {
            getRoutesNeedsPermission.computeIfAbsent(path, k -> new ArrayList<>()).add(permission);
            return this;
        }

        /**
         * Adds a POST route to the builder.
         *
         * @param path the POST route
         * @return the builder instance
         */
        public RestrictedRoutesBuilder post(String path) {
            postRoutes.add(path);
            return this;
        }

        public RestrictedRoutesBuilder postWantsInfo(String path) {
            postRoutesOptional.add(path);
            post(path);
            return this;
        }

        public RestrictedRoutesBuilder postNeedsPermission(String path, String permission) {
            postRoutesNeedsPermission.computeIfAbsent(path, k -> new ArrayList<>()).add(permission);
            return this;
        }

        /**
         * Adds a PUT route to the builder.
         *
         * @param path the PUT route
         * @return the builder instance
         */
        public RestrictedRoutesBuilder put(String path) {
            putRoutes.add(path);
            return this;
        }

        public RestrictedRoutesBuilder putWantsInfo(String path) {
            putRoutesOptional.add(path);
            put(path);
            return this;
        }

        public RestrictedRoutesBuilder putNeedsPermission(String path, String permission) {
            putRoutesNeedsPermission.computeIfAbsent(path, k -> new ArrayList<>()).add(permission);
            return this;
        }

        /**
         * Adds a DELETE route to the builder.
         *
         * @param path the DELETE route
         * @return the builder instance
         */
        public RestrictedRoutesBuilder delete(String path) {
            deleteRoutes.add(path);
            return this;
        }

        public RestrictedRoutesBuilder deleteWantsInfo(String path) {
            deleteRoutesOptional.add(path);
            delete(path);
            return this;
        }

        public RestrictedRoutesBuilder deleteNeedsPermission(String path, String permission) {
            deleteRoutesNeedsPermission.computeIfAbsent(path, k -> new ArrayList<>()).add(permission);
            return this;
        }

        /**
         * Builds and returns a RequestRouteMatcher with the specified routes.
         *
         * @return a new RequestRouteMatcher
         */
        public RequestRouteMatcher build() {
            getRoutes.replaceAll(route -> route.replace("*", PATTERN_STAR));
            postRoutes.replaceAll(route -> route.replace("*", PATTERN_STAR));
            putRoutes.replaceAll(route -> route.replace("*", PATTERN_STAR));
            deleteRoutes.replaceAll(route -> route.replace("*", PATTERN_STAR));
            getRoutesOptional.replaceAll(route -> route.replace("*", PATTERN_STAR));
            postRoutesOptional.replaceAll(route -> route.replace("*", PATTERN_STAR));
            putRoutesOptional.replaceAll(route -> route.replace("*", PATTERN_STAR));
            deleteRoutesOptional.replaceAll(route -> route.replace("*", PATTERN_STAR));
            processNeedsPermissionMap(getRoutesNeedsPermission);
            processNeedsPermissionMap(postRoutesNeedsPermission);
            processNeedsPermissionMap(putRoutesNeedsPermission);
            processNeedsPermissionMap(deleteRoutesNeedsPermission);

            return new RequestRouteMatcher(
                    getRoutes,
                    postRoutes,
                    putRoutes,
                    deleteRoutes,
                    getRoutesOptional,
                    postRoutesOptional,
                    putRoutesOptional,
                    deleteRoutesOptional,
                    getRoutesNeedsPermission,
                    postRoutesNeedsPermission,
                    putRoutesNeedsPermission,
                    deleteRoutesNeedsPermission
            );
        }

        private static void processNeedsPermissionMap(Map<String, List<String>> map) {
            final Map<String, List<String>> tmpMap = new HashMap<>();

            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                String path = entry.getKey().replace("*", PATTERN_STAR);
                List<String> permissions = entry.getValue();
                tmpMap.put(path, permissions);
            }
            map.clear();
            map.putAll(tmpMap);
        }
    }
}