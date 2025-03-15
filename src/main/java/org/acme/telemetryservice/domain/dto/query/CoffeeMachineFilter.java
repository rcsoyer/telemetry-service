package org.acme.telemetryservice.domain.dto.query;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CoffeeMachineFilter(
  @NotNull(message = "It's mandatory to inform which is device to retrieve the data")
  UUID deviceId) {
}
