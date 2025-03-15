package org.acme.telemetryservice.infrastructure.repository;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class CoffeeMachineTelemetryRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private CoffeeMachineTelemetryRepository repository;

    @Test
    void getMachineEventsSummary() {
        repository.getMachineEventsSummary(UUID.randomUUID());
    }
}