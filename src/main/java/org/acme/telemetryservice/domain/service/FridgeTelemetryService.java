package org.acme.telemetryservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.acme.telemetryservice.domain.dto.command.FridgeTelemetryData;
import org.acme.telemetryservice.domain.service.mapper.FridgeTelemetryMapper;
import org.acme.telemetryservice.infrastructure.repository.FridgeTelemetryRepository;
import org.acme.telemetryservice.infrastructure.repository.IoTDeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FridgeTelemetryService {

    private final FridgeTelemetryRepository telemetryRepository;
    private final IoTDeviceRepository deviceRepository;
    private final FridgeTelemetryMapper mapper;

    public void createTelemetryEvent(final FridgeTelemetryData event) {
        deviceRepository
          .findByDeviceId(event.deviceId())
          .map(sourceDevice -> mapper.from(sourceDevice, event))
          .map(telemetryRepository::save)
          .orElseThrow(() -> new IllegalArgumentException("Device not found"));
    }
}
