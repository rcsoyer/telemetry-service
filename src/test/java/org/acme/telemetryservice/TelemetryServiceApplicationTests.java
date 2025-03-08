package org.acme.telemetryservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("inttest")
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TelemetryServiceApplication.class)
@Import(TestcontainersConfiguration.class)
class TelemetryServiceApplicationTests extends BaseTestContainer {

    @Test
    void contextLoads() {
        log.info("Simple test to verify the application runs as expectedly when deployed");
    }
}
