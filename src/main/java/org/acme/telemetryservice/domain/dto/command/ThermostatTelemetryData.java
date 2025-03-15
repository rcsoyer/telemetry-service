package org.acme.telemetryservice.domain.dto.command;

import java.util.UUID;

public record ThermostatTelemetryData(UUID deviceId, double temperature, double humidity)
  implements TelemetryData {
}
