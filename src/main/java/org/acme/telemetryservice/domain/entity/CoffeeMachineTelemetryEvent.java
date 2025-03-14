package org.acme.telemetryservice.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

/**
 * Represents an event telemetry data of a Fridge IoT Device. <br/> Since these are events, these entities are immutable
 */
@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class CoffeeMachineTelemetryEvent extends BaseAuditEventEntity {

    @ManyToOne(optional = false, fetch = LAZY)
    @JoinColumn(name = "device_id", updatable = false)
    private IoTDevice sourceDevice;

    @Enumerated(STRING)
    @NotNull(message = "A status for this event is mandatory")
    private DeviceStatus status;

    @Builder
    private CoffeeMachineTelemetryEvent(final IoTDevice sourceDevice,
                                        final DeviceStatus status) {
        this.sourceDevice = sourceDevice;
        this.status = status;
    }

    public enum DeviceStatus {
        IN_PROGRESS, READY, IDLE, ERROR
    }
}
