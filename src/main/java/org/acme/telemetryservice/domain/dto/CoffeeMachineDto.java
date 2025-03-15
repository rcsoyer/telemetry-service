package org.acme.telemetryservice.domain.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CoffeeMachineDto(@NotNull UUID deviceId,
                               long allTimeTotalCoffeesMade) {
}