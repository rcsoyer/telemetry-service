package org.acme.telemetryservice;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(parallel = true)
public abstract class BaseTestContainer {

    @Container
    @ServiceConnection
    static final PostgresContainer POSTGRES_CONTAINER =
      new PostgresContainer().withReuse(true);

    private static class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {

        private PostgresContainer() {
            super("postgres:latest");
        }
    }
}