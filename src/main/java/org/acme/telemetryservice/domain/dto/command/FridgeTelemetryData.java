package org.acme.telemetryservice.domain.dto.command;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record FridgeTelemetryData(
  @NotNull(message = "The source device's ID is mandatory")
  UUID deviceId,
  double temperature) implements TelemetryData {
}