package org.acme.telemetryservice.domain.dto.command;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent.DeviceStatus;

import static org.apache.commons.lang3.StringUtils.deleteWhitespace;
import static org.apache.commons.lang3.StringUtils.upperCase;

public record CoffeeMachineTelemetryData(
  @NotNull(message = "The source device's ID is mandatory")
  UUID deviceId,
  @EnumType(enumClass = DeviceStatus.class)
  String status) implements TelemetryData {

    public CoffeeMachineTelemetryData(final UUID deviceId, final String status) {
        this.deviceId = deviceId;
        this.status = upperCase(deleteWhitespace(status));
    }
}