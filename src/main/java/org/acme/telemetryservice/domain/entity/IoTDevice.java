package org.acme.telemetryservice.domain.entity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import static lombok.AccessLevel.PROTECTED;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

/**
 * Generic IoT device registration. <br/> Basic, common data expected for all IoT devices registered in the platform
 */
@Getter
@Entity
@Cacheable
@NaturalIdCache
@Cache(usage = READ_WRITE)
@NoArgsConstructor(access = PROTECTED)
public class IoTDevice extends BaseAuditEntity {

    @NaturalId
    @NotNull(message = "A device's ID is mandatory")
    private UUID deviceId;

    @NotBlank(message = "A device's nick name in the platform is mandatory")
    private String deviceName;

    @NotBlank(message = "The device model's name as defined by its manufacturer")
    private String deviceModel;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "A valid IoT device Type is mandatory")
    private IotDeviceType deviceType;

    @Builder
    private IoTDevice(final String deviceName,
                      final String deviceModel,
                      final IotDeviceType deviceType) {
        this.deviceName = capitalize(normalizeSpace(deviceName));
        this.deviceModel = capitalize(normalizeSpace(deviceModel));
        this.deviceType = deviceType;
        this.deviceId = UUID.randomUUID();
    }

    enum IotDeviceType {
        FRIDGE, COFFEE_MACHINE, THERMOSTAT
    }
}
