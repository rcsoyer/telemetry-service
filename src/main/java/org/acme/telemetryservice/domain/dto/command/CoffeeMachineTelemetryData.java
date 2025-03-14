package org.acme.telemetryservice.domain.dto.command;

import java.util.UUID;
import org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent.DeviceStatus;

public record CoffeeMachineTelemetryData(
  UUID deviceId,
  @EnumType(enumClass = DeviceStatus.class)
  String status) implements TelemetryData {
}