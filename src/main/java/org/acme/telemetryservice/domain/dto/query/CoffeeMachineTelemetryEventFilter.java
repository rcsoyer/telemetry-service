package org.acme.telemetryservice.domain.dto.query;

import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static java.time.ZoneId.systemDefault;

public record CoffeeMachineTelemetryEventFilter(
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
                 ? startDate().atStartOfDay(systemDefault()).toInstant()
                 : null;
    }

    public Instant getEndDate() {
        return endDate() != null
                 ? endDate().atStartOfDay(systemDefault()).toInstant()
                 : null;
    }
}

