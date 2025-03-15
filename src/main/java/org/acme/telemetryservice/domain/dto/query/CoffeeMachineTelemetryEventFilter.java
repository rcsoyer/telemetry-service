package org.acme.telemetryservice.domain.dto.query;

import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

public record CoffeeMachineTelemetryEventFilter(
  @NotNull
  UUID deviceId, Period period) {

    private boolean hasPeriod() {
        return period != null &&
                 period.startDate() != null &&
                 period.endDate() != null;
    }

    public Optional<Period> getPeriod() {
        if (hasPeriod()) {
            return Optional.of(period);
        }

        return Optional.empty();
    }
}

