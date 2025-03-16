package org.acme.telemetryservice.domain.service;

import org.acme.telemetryservice.domain.dto.command.FridgeTelemetryData;
import org.acme.telemetryservice.domain.dto.query.FridgeSummary;
import org.acme.telemetryservice.domain.dto.query.TelemetryEventFilter;
import org.acme.telemetryservice.domain.entity.FridgeTelemetryEvent;
import org.acme.telemetryservice.domain.service.mapper.FridgeTelemetryMapper;
import org.acme.telemetryservice.infrastructure.repository.FridgeTelemetryRepository;
import org.acme.telemetryservice.infrastructure.repository.IoTDeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public non-sealed class FridgeTelemetryService
  extends TelemetryService<FridgeTelemetryEvent, FridgeTelemetryData> {

    private final FridgeTelemetryRepository telemetryRepository;
    private final FridgeTelemetryMapper mapper;

    public FridgeTelemetryService(final IoTDeviceRepository deviceRepository,
                                  final FridgeTelemetryRepository telemetryRepository,
                                  final FridgeTelemetryMapper mapper) {
        super(deviceRepository);
        this.telemetryRepository = telemetryRepository;
        this.mapper = mapper;
    }

    public void createTelemetryEvent(final FridgeTelemetryData event) {
        createTelemetryEvent(event, mapper::from, telemetryRepository::save);
    }

    @Transactional(readOnly = true)
    public FridgeSummary summarize(final TelemetryEventFilter filter) {
        return null;
    }
}
