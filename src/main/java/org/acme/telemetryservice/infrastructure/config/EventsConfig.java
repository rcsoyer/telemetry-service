package org.acme.telemetryservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;

@Configuration
class EventsConfig {

    /**
     * Set up for the Spring framework to publish and handle events asynchronously
     */
    @Bean
    ApplicationEventMulticaster applicationEventMulticaster(
      @Qualifier("applicationTaskExecutor") final AsyncTaskExecutor virtualThreadAsyncTaskExecutor) {
        final var eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(virtualThreadAsyncTaskExecutor);
        return eventMulticaster;
    }

    @Bean
    AuthenticationEventPublisher authenticationEventPublisher(
      final ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }
}
