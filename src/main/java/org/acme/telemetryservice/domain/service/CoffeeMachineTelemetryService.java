package org.acme.telemetryservice.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.command.CoffeeMachineTelemetryData;
import org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent;
import org.acme.telemetryservice.domain.entity.IoTDevice;
import org.acme.telemetryservice.domain.service.mapper.CoffeeMachineTelemetryMapper;
import org.acme.telemetryservice.infrastructure.repository.CoffeeMachineTelemetryRepository;
import org.acme.telemetryservice.infrastructure.repository.IoTDeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class CoffeeMachineTelemetryService
  extends TelemetryService<CoffeeMachineTelemetryEvent, CoffeeMachineTelemetryData> {

    private final CoffeeMachineTelemetryRepository telemetryRepository;
    private final CoffeeMachineTelemetryMapper mapper;

    public CoffeeMachineTelemetryService(final IoTDeviceRepository deviceRepository,
                                         final CoffeeMachineTelemetryRepository telemetryRepository,
                                         final CoffeeMachineTelemetryMapper mapper) {
        super(deviceRepository);
        this.telemetryRepository = telemetryRepository;
        this.mapper = mapper;
    }

    @Override
    CoffeeMachineTelemetryEvent mapToEntity(
      final IoTDevice sourceDevice,
      final CoffeeMachineTelemetryData telemetryData) {
        return mapper.from(sourceDevice, telemetryData);
    }

    @Override
    CoffeeMachineTelemetryEvent persistTelemetryEvent(final CoffeeMachineTelemetryEvent event) {
        return telemetryRepository.save(event);
    }
}
