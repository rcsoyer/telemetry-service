package org.acme.telemetryservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.acme.telemetryservice.domain.dto.command.TelemetryData;
import org.acme.telemetryservice.domain.entity.BaseAuditEventEntity;
import org.acme.telemetryservice.domain.entity.IoTDevice;
import org.acme.telemetryservice.infrastructure.repository.IoTDeviceRepository;

@RequiredArgsConstructor
abstract class TelemetryService<E extends BaseAuditEventEntity,
                                 T extends TelemetryData> {

    private final IoTDeviceRepository deviceRepository;

    public void createTelemetryEvent(final T event) {
        deviceRepository
          .findByDeviceId(event.deviceId())
          .map(sourceDevice -> mapToEntity(sourceDevice, event))
          .map(this::persistTelemetryEvent)
          .orElseThrow(() -> new IllegalArgumentException("Device not found"));
    }

    abstract E mapToEntity(IoTDevice sourceDevice, T telemetryData);

    abstract E persistTelemetryEvent(E entity);
}
