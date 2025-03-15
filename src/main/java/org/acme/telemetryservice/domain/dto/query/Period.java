package org.acme.telemetryservice.domain.dto.query;

import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record Period(@PastOrPresent(message = "The start date of the period cannot be in the future")
                     LocalDate startDate,
                     @PastOrPresent(message = "The end date of the period cannot be in the future")
                     LocalDate endDate) {
}
