package com.klnsdr.axon.auth;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for matching HTTP request routes to determine if they are restricted.
 */
public class RequestRouteMatcher {
    private final List<String> getRoutes;
    private final List<String> postRoutes;
    private final List<String> putRoutes;
    private final List<String> deleteRoutes;

    /**
     * Constructs a new RequestRouteMatcher with the specified routes for each HTTP method.
     *
     * @param getRoutes the list of GET routes
     * @param postRoutes the list of POST routes
     * @param putRoutes the list of PUT routes
     * @param deleteRoutes the list of DELETE routes
     */
    public RequestRouteMatcher(List<String> getRoutes, List<String> postRoutes, List<String> putRoutes, List<String> deleteRoutes) {
        this.getRoutes = getRoutes;
        this.postRoutes = postRoutes;
        this.putRoutes = putRoutes;
        this.deleteRoutes = deleteRoutes;
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
            return new RequestRouteMatcher(getRoutes, postRoutes, putRoutes, deleteRoutes);
        }
    }
}