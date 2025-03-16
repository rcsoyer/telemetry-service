package org.acme.telemetryservice.domain.dto.query;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.acme.telemetryservice.domain.entity.FridgeTelemetryEvent;

/**
 * Projection representing the summary telemetry data of {@link FridgeTelemetryEvent}s
 * linked to {@link FridgeTelemetryEvent#getSourceDevice()}
 */
public interface FridgeSummary {

    @NotNull
    UUID getDeviceId();

    double getAvgTemperature();

    double getMaxTemperature();

    double getMinTemperature();
}