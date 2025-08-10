package com.klnsdr.axon.auth.token;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.klnsdr.axon.auth.token.service.TokenService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;

public class TokenCleanupTaskTest {
    @Test
    void cleanupLogsInfoMessage() {
        TokenService tokenService = mock(TokenService.class);
        TokenCleanupTask task = new TokenCleanupTask(tokenService);
        Logger logger = LoggerFactory.getLogger(TokenCleanupTask.class);
        Appender<ILoggingEvent> appender = mock(Appender.class);
        when(appender.getName()).thenReturn("MOCK");
        ((ch.qos.logback.classic.Logger) logger).addAppender(appender);

        task.cleanup();

        verify(appender, times(1)).doAppend(argThat(event -> event.getFormattedMessage().contains("Cleaning up expired tokens")));
    }

    @Test
    void cleanupCallsDeleteExpiredOnTokenService() {
        TokenService tokenService = mock(TokenService.class);
        TokenCleanupTask task = new TokenCleanupTask(tokenService);

        task.cleanup();

        verify(tokenService, times(1)).deleteExpired();
    }
}
