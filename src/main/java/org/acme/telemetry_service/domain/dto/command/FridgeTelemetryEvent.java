package org.acme.telemetry_service.domain.dto.command;

import java.util.Map;

public record FridgeTelemetryEvent(int deviceId, double temperature,
                                   Map<String, Integer> groceries) {
}
