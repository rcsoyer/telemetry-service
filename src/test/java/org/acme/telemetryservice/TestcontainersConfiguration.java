package org.acme.telemetryservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
class TestcontainersConfiguration {

    @Container
    private static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(
      DockerImageName.parse("apache/kafka:latest")
    ).withReuse(true);

    @DynamicPropertySource
    private static void overrideProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    }
}
