package com.klnsdr.axon;

import com.klnsdr.axon.auth.JwtAuthPreFilter;
import com.klnsdr.axon.auth.OAuthHandler;
import com.klnsdr.axon.auth.token.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
@ComponentScan("com.klnsdr.axon")
@EnableWebSecurity
public class BackendApplicationConfig implements WebMvcConfigurer {
    @Value("${app.oauth2.successRedirectUrl}")
    private String oauthSuccessRedirectUrl;

    @Value("${app.oauth2.failRedirectUrl}")
    private String oauthFailureRedirectUrl;

    private final OAuthHandler oAuthHandler;
    private final JwtAuthPreFilter jwtAuthPreFilter;
    private final TokenService tokenService;

    public BackendApplicationConfig(OAuthHandler oAuthHandler, JwtAuthPreFilter jwtAuthPreFilter, TokenService tokenService) {
        this.oAuthHandler = oAuthHandler;
        this.jwtAuthPreFilter = jwtAuthPreFilter;
        this.tokenService = tokenService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                    .anyRequest().permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                    .successHandler(oAuthHandler)
                    .failureUrl(oauthFailureRedirectUrl)
            ).logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/api/users/logout", "GET"))
                .logoutSuccessUrl(oauthSuccessRedirectUrl)
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(new DeleteTokenLogoutSuccessHandler(tokenService, oauthSuccessRedirectUrl))
                .permitAll()
            )
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sessionManagement -> sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ).addFilterBefore(jwtAuthPreFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true);
    }

    private static class DeleteTokenLogoutSuccessHandler implements LogoutSuccessHandler {
        private final TokenService tokenService;
        private final String redirectUrl;

        public DeleteTokenLogoutSuccessHandler(TokenService tokenService, String redirectUrl) {
            this.tokenService = tokenService;
            this.redirectUrl = redirectUrl;
        }

        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
            final String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                final String token = authHeader.substring(7);
                tokenService.removeToken(token);
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect(redirectUrl);
        }
    }
}
