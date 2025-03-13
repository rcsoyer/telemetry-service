package org.acme.telemetryservice.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;

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

    @Override
    public boolean equals(final Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final var anotherFridge = (FridgeTelemetryData) other;
        if (allNotNull(getId(), anotherFridge.getId())) {
            return getId().equals(anotherFridge.getId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
