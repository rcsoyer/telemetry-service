package org.acme.telemetryservice.domain.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.command.CoffeeMachineTelemetryData;
import org.acme.telemetryservice.domain.dto.query.CoffeeMachineStatusSummary;
import org.acme.telemetryservice.domain.dto.query.CoffeeMachineTelemetryEventFilter;
import org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent;
import org.acme.telemetryservice.domain.service.mapper.CoffeeMachineTelemetryMapper;
import org.acme.telemetryservice.infrastructure.repository.CoffeeMachineTelemetryRepository;
import org.acme.telemetryservice.infrastructure.repository.IoTDeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public non-sealed class CoffeeMachineTelemetryService
  extends TelemetryService<CoffeeMachineTelemetryEvent, CoffeeMachineTelemetryData> {

    private final CoffeeMachineTelemetryRepository repository;
    private final CoffeeMachineTelemetryMapper mapper;

    public CoffeeMachineTelemetryService(final IoTDeviceRepository deviceRepository,
                                         final CoffeeMachineTelemetryRepository telemetryRepository,
                                         final CoffeeMachineTelemetryMapper mapper) {
        super(deviceRepository);
        this.repository = telemetryRepository;
        this.mapper = mapper;
    }

    @Override
    public void createTelemetryEvent(final CoffeeMachineTelemetryData event) {
        createTelemetryEvent(event, mapper::from, repository::save);
    }

    @Transactional(readOnly = true)
    public List<CoffeeMachineStatusSummary> summarize(
      final CoffeeMachineTelemetryEventFilter filter) {
        return repository.getMachineEventsSummary(
          filter.deviceId(), filter.getStartDate(), filter.getEndDate()
        );
    }
}
