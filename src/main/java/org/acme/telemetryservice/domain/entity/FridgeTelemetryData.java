package org.acme.telemetryservice.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

/**
 * Fridge telemetry data events. <br/> Since these are events, these entities are immutable
 */
@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class FridgeTelemetryData extends BaseAuditImmutableEntity {

    @ManyToOne(optional = false, fetch = LAZY)
    private IoTDevice device;

    private double temperature;

    @Builder
    private FridgeTelemetryData(final IoTDevice device, final double temperature) {
        this.device = device;
        this.temperature = temperature;
    }
}
