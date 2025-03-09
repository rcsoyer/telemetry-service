package org.acme.telemetryservice.domain.dto.command;

public record CoffeeMachineTelemetryEvent(DeviceStatus status) {

    enum DeviceStatus {
        IN_PROGRESS, READY, IDLE, ERROR
    }
}
