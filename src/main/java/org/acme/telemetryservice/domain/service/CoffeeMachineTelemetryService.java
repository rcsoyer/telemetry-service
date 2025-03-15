package org.acme.telemetryservice.domain.service;

import java.util.UUID;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.command.CoffeeMachineTelemetryData;
import org.acme.telemetryservice.domain.dto.query.CoffeeMachineTotalCoffeesMade;
import org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent;
import org.acme.telemetryservice.domain.service.mapper.CoffeeMachineTelemetryMapper;
import org.acme.telemetryservice.infrastructure.repository.CoffeeMachineTelemetryRepository;
import org.acme.telemetryservice.infrastructure.repository.IoTDeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@Transactional
public non-sealed class CoffeeMachineTelemetryService extends TelemetryService<CoffeeMachineTelemetryEvent, CoffeeMachineTelemetryData> {

    private final CoffeeMachineTelemetryRepository repository;
    private final CoffeeMachineTelemetryMapper mapper;

    public CoffeeMachineTelemetryService(final IoTDeviceRepository deviceRepository, final CoffeeMachineTelemetryRepository telemetryRepository,
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
    public CoffeeMachineTotalCoffeesMade countTotalCoffeesMade(final UUID deviceId) {
        return repository
                 .countTotalCoffeesMade(deviceId)
                 .orElseThrow(noDeviceFound(deviceId));
    }

    private Supplier<ResponseStatusException> noDeviceFound(final UUID deviceId) {
        return () -> {
            log.warn("Client of the API provided a deviceId that it's not registered."
                       + "Or the machine never finished making coffees. "
                       + "deviceId={}",
                     deviceId);
            return new ResponseStatusException(NOT_FOUND, "No information available for this device");
        };
    }
}
