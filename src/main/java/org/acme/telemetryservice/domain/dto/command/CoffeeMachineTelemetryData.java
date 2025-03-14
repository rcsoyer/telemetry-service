package org.acme.telemetryservice.domain.dto.command;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent.DeviceStatus;

public record CoffeeMachineTelemetryData(
  @NotNull(message = "The source device's ID is mandatory")
  UUID deviceId,
  @EnumType(enumClass = DeviceStatus.class)
  String status) implements TelemetryData {
}