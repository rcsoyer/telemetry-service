package org.acme.telemetryservice.domain.dto.query;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;

public record CoffeeMachineTelemetryEventFilter(
  @PathVariable
  @NotNull(message = "The source device ID is mandatory")
  UUID deviceId,
  @Nullable @Valid
  Period period) {

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

