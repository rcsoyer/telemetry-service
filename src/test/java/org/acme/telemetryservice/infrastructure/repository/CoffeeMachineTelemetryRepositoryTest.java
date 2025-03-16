package org.acme.telemetryservice.infrastructure.repository;

import java.time.Instant;
import java.util.List;
import org.acme.telemetryservice.domain.dto.query.CoffeeMachineStatusSummary;
import org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent;
import org.acme.telemetryservice.domain.entity.IoTDevice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent.DeviceStatus.READY;
import static org.acme.telemetryservice.domain.entity.IoTDevice.IotDeviceType.COFFEE_MACHINE;
import static org.assertj.core.api.Assertions.assertThat;

class CoffeeMachineTelemetryRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private CoffeeMachineTelemetryRepository repository;

    @Autowired
    private IoTDeviceRepository ioTDeviceRepository;

    private IoTDevice sourceCoffeeMachine;

    @BeforeEach
    void setUp() {
        this.sourceCoffeeMachine = IoTDevice
                                     .builder()
                                     .deviceType(COFFEE_MACHINE)
                                     .deviceModel("Coffee Machine 200xyz Lavaza")
                                     .deviceName("House coffee maker")
                                     .build();
        ioTDeviceRepository.save(sourceCoffeeMachine);
    }

    @Nested
    class GetMachineEventsSummary {

        @Test
        void getMachineEventsSummary_allFilters() {
            final var telemetryEvent = CoffeeMachineTelemetryEvent
                                         .builder()
                                         .sourceDevice(sourceCoffeeMachine)
                                         .status(READY)
                                         .build();
            repository.save(telemetryEvent);

            final List<CoffeeMachineStatusSummary> result =
              repository.getMachineEventsSummary(sourceCoffeeMachine.getDeviceId(),
                                                 Instant.parse("2025-01-01T10:15:30.00Z"),
                                                 Instant.now());

            assertThat(result)
              .hasSize(1)
              .satisfiesExactly(
                summaryReady ->
                  assertThat(summaryReady)
                    .hasFieldOrPropertyWithValue("deviceId", sourceCoffeeMachine.getDeviceId()));
        }

    }
}