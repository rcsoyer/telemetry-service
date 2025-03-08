package org.acme.telemetryservice;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class BaseTestContainer {

    private static final PostgresContainer POSTGRES_CONTAINER;

    static {
        POSTGRES_CONTAINER = new PostgresContainer()
                               .withUsername("root")
                               .withPassword("root")
                               .withReuse(true);
        POSTGRES_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
    }

    private static class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {

        private PostgresContainer() {
            super("postgres:latest");
        }
    }
}