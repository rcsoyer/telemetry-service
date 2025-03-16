package org.acme.telemetryservice.infrastructure.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.acme.telemetryservice.domain.dto.query.CoffeeMachineStatusSummary;
import org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent;
import org.acme.telemetryservice.domain.entity.IoTDevice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent.DeviceStatus.ERROR;
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
            final var eventReady = CoffeeMachineTelemetryEvent
                                     .builder()
                                     .sourceDevice(sourceCoffeeMachine)
                                     .status(READY)
                                     .build();
            repository.save(eventReady);

            final var eventError = CoffeeMachineTelemetryEvent
                                     .builder()
                                     .sourceDevice(sourceCoffeeMachine)
                                     .status(ERROR)
                                     .build();
            repository.save(eventError);

            final UUID deviceId = sourceCoffeeMachine.getDeviceId();

            final List<CoffeeMachineStatusSummary> result =
              repository.getMachineEventsSummary(deviceId,
                                                 Instant.parse("2025-01-01T10:15:30.00Z"),
                                                 Instant.now());

            assertThat(result)
              .hasSize(2)
              .satisfiesExactly(
                summaryReady ->
                  assertThat(summaryReady)
                    .hasFieldOrPropertyWithValue("deviceId", deviceId),
                summaryError ->
                  assertThat(summaryError)
                    .hasFieldOrPropertyWithValue("deviceId", deviceId)
              );
        }

    }
}