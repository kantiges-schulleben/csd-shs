package com.klnsdr.axon.auth.token;

import com.klnsdr.axon.auth.token.service.TokenService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenCleanupTask {
    private static final Logger log = LoggerFactory.getLogger(TokenCleanupTask.class);
    private final TokenService tokenService;

    /**
     * Constructs a new TokenCleanupTask with the specified token service.
     *
     * @param tokenService the token service
     */
    public TokenCleanupTask(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Scheduled method that runs at a fixed delay to clean up expired tokens.
     * Logs the cleanup process and delegates the deletion of expired tokens to the token service.
     */
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void cleanup() {
        log.info("Cleaning up expired tokens");
        tokenService.deleteExpired();
    }
}
