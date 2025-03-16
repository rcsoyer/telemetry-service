package org.acme.telemetryservice.domain.service;

import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.command.ThermostatTelemetryData;
import org.acme.telemetryservice.domain.dto.query.TelemetryEventFilter;
import org.acme.telemetryservice.domain.dto.query.ThermostatSummary;
import org.acme.telemetryservice.domain.entity.ThermostatTelemetryEvent;
import org.acme.telemetryservice.domain.service.mapper.ThermostatTelemetryMapper;
import org.acme.telemetryservice.infrastructure.repository.IoTDeviceRepository;
import org.acme.telemetryservice.infrastructure.repository.ThermostatTelemetryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@Transactional
public non-sealed class ThermostatTelemetryService
  extends TelemetryService<ThermostatTelemetryEvent, ThermostatTelemetryData> {

    private final ThermostatTelemetryRepository repository;
    private final ThermostatTelemetryMapper mapper;

    public ThermostatTelemetryService(final IoTDeviceRepository deviceRepository,
                                      final ThermostatTelemetryRepository repository,
                                      final ThermostatTelemetryMapper mapper) {
        super(deviceRepository);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void createTelemetryEvent(final ThermostatTelemetryData event) {
        createTelemetryEvent(event, mapper::from, repository::save);
    }

    @Transactional(readOnly = true)
    public ThermostatSummary summarize(final TelemetryEventFilter filter) {
        return repository
                 .getMachineEventsSummary(
                   filter.deviceId(), filter.getStartDate(), filter.getEndDate()
                 )
                 .orElseThrow(notFoundMatchingFilter(filter));
    }

    private Supplier<ResponseStatusException> notFoundMatchingFilter(
      final TelemetryEventFilter filter) {
        return () -> {
            log.warn("No data found matching filter={}", filter);
            return
              new ResponseStatusException(NOT_FOUND,
                                          "No data found matching provided query parameters");
        };
    }
}