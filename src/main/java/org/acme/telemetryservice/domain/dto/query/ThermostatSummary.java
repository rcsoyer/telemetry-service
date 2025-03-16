package org.acme.telemetryservice.domain.dto.query;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.acme.telemetryservice.domain.entity.ThermostatTelemetryEvent;

/**
 * Projection representing the summary telemetry data of {@link ThermostatTelemetryEvent}s
 * linked to {@link ThermostatTelemetryEvent#getSourceDevice()}
 */
public interface ThermostatSummary {

    @NotNull
    UUID getDeviceId();

    double getAvgTemperature();

    double getAvgHumidity();

    double getMaxTemperature();

    double getMinTemperature();
}