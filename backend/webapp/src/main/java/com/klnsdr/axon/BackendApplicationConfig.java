package com.klnsdr.axon;

import com.klnsdr.axon.auth.JwtAuthPreFilter;
import com.klnsdr.axon.auth.OAuthHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    public BackendApplicationConfig(OAuthHandler oAuthHandler, JwtAuthPreFilter jwtAuthPreFilter) {
        this.oAuthHandler = oAuthHandler;
        this.jwtAuthPreFilter = jwtAuthPreFilter;
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
                .allowedOrigins("localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true);
    }
}
