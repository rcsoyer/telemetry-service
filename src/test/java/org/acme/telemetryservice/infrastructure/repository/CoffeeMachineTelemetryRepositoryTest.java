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
import org.springframework.test.util.ReflectionTestUtils;

import static org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent.DeviceStatus.ERROR;
import static org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent.DeviceStatus.IN_PROGRESS;
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

        @BeforeEach
        void setUp() {
            final var eventOutOfPeriod = CoffeeMachineTelemetryEvent
                                           .builder()
                                           .sourceDevice(sourceCoffeeMachine)
                                           .status(READY)
                                           .build();

            repository.saveAll(List.of(
              CoffeeMachineTelemetryEvent
                .builder()
                .sourceDevice(sourceCoffeeMachine)
                .status(READY)
                .build(),
              CoffeeMachineTelemetryEvent
                .builder()
                .sourceDevice(sourceCoffeeMachine)
                .status(ERROR)
                .build(),
              CoffeeMachineTelemetryEvent
                .builder()
                .sourceDevice(sourceCoffeeMachine)
                .status(IN_PROGRESS)
                .build(),
              eventOutOfPeriod
            ));

            ReflectionTestUtils
              .setField(eventOutOfPeriod, "createdAt",
                        Instant.parse("2019-02-03T10:15:30.00Z"));

            repository.save(eventOutOfPeriod);
        }

        @Test
        void getMachineEventsSummary_allFilters() {
            final UUID deviceId = sourceCoffeeMachine.getDeviceId();

            final List<CoffeeMachineStatusSummary> result =
              repository.getMachineEventsSummary(deviceId,
                                                 Instant.parse("2025-01-01T10:15:30.00Z"),
                                                 Instant.now());

            assertThat(result)
              .hasSize(2)
              .satisfiesExactlyInAnyOrder(
                summaryReady ->
                  assertThat(summaryReady)
                    .hasFieldOrPropertyWithValue("deviceId", deviceId)
                    .hasFieldOrPropertyWithValue("deviceStatus", READY)
                    .hasFieldOrPropertyWithValue("totalCount", 1),
                summaryError ->
                  assertThat(summaryError)
                    .hasFieldOrPropertyWithValue("deviceId", deviceId)
                    .hasFieldOrPropertyWithValue("deviceStatus", ERROR)
                    .hasFieldOrPropertyWithValue("totalCount", 1)
              );
        }

        @Test
        void getMachineEventsSummary_deviceIdFilter() {
            final UUID deviceId = sourceCoffeeMachine.getDeviceId();

            final List<CoffeeMachineStatusSummary> result =
              repository.getMachineEventsSummary(deviceId, null, null);

            assertThat(result)
              .hasSize(2)
              .satisfiesExactlyInAnyOrder(
                summaryReady ->
                  assertThat(summaryReady)
                    .hasFieldOrPropertyWithValue("deviceId", deviceId)
                    .hasFieldOrPropertyWithValue("deviceStatus", READY)
                    .hasFieldOrPropertyWithValue("totalCount", 2),
                summaryError ->
                  assertThat(summaryError)
                    .hasFieldOrPropertyWithValue("deviceId", deviceId)
                    .hasFieldOrPropertyWithValue("deviceStatus", ERROR)
                    .hasFieldOrPropertyWithValue("totalCount", 1)
              );
        }

    }
}