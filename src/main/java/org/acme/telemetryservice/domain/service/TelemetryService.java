package org.acme.telemetryservice.domain.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.command.TelemetryData;
import org.acme.telemetryservice.domain.entity.BaseAuditEventEntity;
import org.acme.telemetryservice.domain.entity.IoTDevice;
import org.acme.telemetryservice.infrastructure.repository.IoTDeviceRepository;

@Slf4j
@RequiredArgsConstructor
abstract class TelemetryService<E extends BaseAuditEventEntity,
                                 T extends TelemetryData> {

    private final IoTDeviceRepository deviceRepository;

    public void createTelemetryEvent(final T event) {
        deviceRepository
          .findByDeviceId(event.deviceId())
          .map(sourceDevice -> mapToEntity(sourceDevice, event))
          .map(this::persistTelemetryEvent)
          .orElseThrow(() -> iotSourceDeviceNotFound(event.deviceId()));
    }

    private IllegalArgumentException iotSourceDeviceNotFound(final UUID sourceId) {
        log.warn("Invalid deviceId={} passed as event source", sourceId);
        return new IllegalArgumentException("Device not found");
    }

    abstract E mapToEntity(IoTDevice sourceDevice, T telemetryData);

    abstract E persistTelemetryEvent(E entity);
}
