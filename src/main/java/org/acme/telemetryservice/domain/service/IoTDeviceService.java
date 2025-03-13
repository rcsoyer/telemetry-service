package org.acme.telemetryservice.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.command.IoTDeviceRegisterRequest;
import org.acme.telemetryservice.domain.dto.command.IoTDeviceRegisterResponse;
import org.acme.telemetryservice.domain.entity.IoTDevice;
import org.acme.telemetryservice.domain.service.mapper.IoTDeviceMapper;
import org.acme.telemetryservice.infrastructure.repository.IoTDeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class IoTDeviceService {

    private final IoTDeviceRepository repository;
    private final IoTDeviceMapper mapper;

    public IoTDeviceRegisterResponse createDevice(final IoTDeviceRegisterRequest request) {
        checkDeviceNameAvailability(request.deviceName());
        final IoTDevice registeredDevice = repository.save(mapper.from(request));
        return mapper.to(registeredDevice);
    }

    private void checkDeviceNameAvailability(final String deviceName) {
        log.debug("Check if the deviceName is available");
        if (repository.existsByDeviceName(deviceName)) {
            log.warn("Device with name {} already exists", deviceName);
            throw new ResponseStatusException(CONFLICT, "The provided device name is already in use");
        }
    }
}
