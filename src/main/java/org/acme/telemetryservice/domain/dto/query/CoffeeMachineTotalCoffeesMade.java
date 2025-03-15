package org.acme.telemetryservice.domain.dto.query;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public interface CoffeeMachineTotalCoffeesMade {

    @NotNull
    UUID getDeviceId();

    long getAllTimeTotalCoffeesMade();
}