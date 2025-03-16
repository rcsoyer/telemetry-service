package org.acme.telemetryservice.domain.dto.query;

import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static java.time.ZoneOffset.UTC;

public record TelemetryEventFilter(
  @PathVariable
  UUID deviceId,
  @PastOrPresent(message = "The start date of the period cannot be in the future")
  @RequestParam(required = false)
  LocalDate startDate,
  @PastOrPresent(message = "The end date of the period cannot be in the future")
  @RequestParam(required = false)
  LocalDate endDate) {

    public Instant getStartDate() {
        return startDate() != null
                 ? startDate().atStartOfDay(UTC).toInstant()
                 : null;
    }

    public Instant getEndDate() {
        return endDate() != null
                 ? endDate().atTime(23, 59, 59)
                            .atZone(UTC)
                            .toInstant()
                 : null;
    }
}

