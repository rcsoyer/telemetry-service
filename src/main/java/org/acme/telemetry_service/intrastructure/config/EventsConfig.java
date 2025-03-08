package org.acme.telemetry_service.intrastructure.config;

import org.springframework.boot.autoconfigure.context.LifecycleProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;

@Configuration
class EventsConfig {

    @Bean
    @Primary
    AsyncTaskExecutor virtualThreadAsyncTaskExecutor(
      final LifecycleProperties lifecycleProperties) {
        final var asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setVirtualThreads(true);
        final long taskTerminationTimeOut = lifecycleProperties.getTimeoutPerShutdownPhase().toMillis();
        asyncTaskExecutor.setTaskTerminationTimeout(taskTerminationTimeOut);
        return asyncTaskExecutor;
    }

    /**
     * Set up for the Spring framework to publish and handle events asynchronously
     */
    @Bean
    ApplicationEventMulticaster applicationEventMulticaster(
      final AsyncTaskExecutor asyncTaskExecutor) {
        final var eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(asyncTaskExecutor);
        return eventMulticaster;
    }

    @Bean
    AuthenticationEventPublisher authenticationEventPublisher(
      final ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }
}
