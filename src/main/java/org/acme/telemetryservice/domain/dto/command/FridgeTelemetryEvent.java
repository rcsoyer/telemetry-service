package org.acme.telemetryservice.domain.dto.command;

public record FridgeTelemetryEvent(int deviceId, double temperature) {
}
