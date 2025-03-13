package org.acme.telemetryservice.domain.service.mapper;

import org.acme.telemetryservice.domain.dto.command.IoTDeviceRegisterRequest;
import org.acme.telemetryservice.domain.dto.command.IoTDeviceRegisterResponse;
import org.acme.telemetryservice.domain.entity.IoTDevice;
import org.mapstruct.Mapper;

@Mapper
public interface IoTDeviceMapper {

    IoTDevice from(IoTDeviceRegisterRequest request);

    IoTDeviceRegisterResponse to(IoTDevice device);
}
