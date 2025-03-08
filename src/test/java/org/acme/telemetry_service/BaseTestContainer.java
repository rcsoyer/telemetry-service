package org.acme.telemetry_service;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class BaseTestContainer {

    private static final PostgresContainer POSTGRES_CONTAINER;
    //private static final KafkaContainer KAFKA_CONTAINER;

    static {
        POSTGRES_CONTAINER = new PostgresContainer()
                               .withUsername("root")
                               .withPassword("root")
                               .withReuse(true);
        POSTGRES_CONTAINER.start();

     /*   KAFKA_CONTAINER = new KafkaContainer(DockerImageName.parse("apache/kafka:latest"));
        KAFKA_CONTAINER.start();*/
    }

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
       // registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    }

    private static class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {

        private PostgresContainer() {
            super("postgres:latest");
        }
    }
}