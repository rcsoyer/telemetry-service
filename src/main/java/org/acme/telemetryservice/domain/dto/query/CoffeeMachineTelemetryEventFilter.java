package org.acme.telemetryservice.domain.dto.query;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record CoffeeMachineTelemetryEventFilter(
  @NotNull
  UUID deviceId, Period period) {

    public Instant startDate() {
        return period != null && period.startDate() != null
                 ? Instant.from(period.startDate())
                 : null;
    }

    public Instant endDate() {
        return period != null && period.endDate() != null
                 ? Instant.from(period.endDate())
                 : null;
    }
}

