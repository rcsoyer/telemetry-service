package org.acme.telemetryservice.domain.dto.query;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent;
import org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent.DeviceStatus;

/**
 * Projection representing the summary telemetry data of {@link CoffeeMachineTelemetryEvent}
 * grouped by {@link CoffeeMachineTelemetryEvent#getSourceDevice()}
 * and {@link CoffeeMachineTelemetryEvent#getStatus()}
 */
public interface CoffeeMachineStatusSummary {

    @NotNull
    UUID getDeviceId();

    int getTotalCount();

    @NotNull
    DeviceStatus getDeviceStatus();
}