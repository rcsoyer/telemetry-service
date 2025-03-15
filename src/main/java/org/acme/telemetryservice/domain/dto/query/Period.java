package org.acme.telemetryservice.domain.dto.query;

import java.time.LocalDate;

public record Period(LocalDate startDate, LocalDate endDate) {
}
