package org.acme.telemetryservice.domain.dto.command;

import jakarta.validation.constraints.NotNull;

public record AccountRegisterResponse(@NotNull String username, @NotNull String password) {
}
