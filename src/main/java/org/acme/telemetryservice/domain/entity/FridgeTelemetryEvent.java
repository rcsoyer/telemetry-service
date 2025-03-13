package org.acme.telemetryservice.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

/**
 * Represents an event telemetry data of a Fridge IoT Device.
 * <br/> Since these are events, these entities are immutable
 */
@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class FridgeTelemetryEvent extends BaseAuditEventEntity {

    @ManyToOne(optional = false, fetch = LAZY)
    @JoinColumn(name = "device_id", updatable = false)
    private IoTDevice sourceDevice;

    private double temperature;

    @Builder
    private FridgeTelemetryEvent(final IoTDevice sourceDevice, final double temperature) {
        this.sourceDevice = sourceDevice;
        this.temperature = temperature;
    }
}
