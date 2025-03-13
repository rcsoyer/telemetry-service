package org.acme.telemetryservice.domain.dto.command;

import java.util.UUID;

public record FridgeTelemetryData(UUID deviceId, double temperature) {
}
