package org.acme.telemetryservice.domain.dto.command;

public record ThermostatTelemetryEvent(double temperature, double humidity) {
}
