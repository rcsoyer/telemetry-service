package org.acme.telemetryservice.domain.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.acme.telemetryservice.domain.entity.IoTDevice.IotDeviceType;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;

public record IoTDeviceRegisterRequest(
  @NotBlank(message = "A device's name in the platform is mandatory")
  String deviceName,
  @NotBlank(message = "The device model's name as defined by its manufacturer, it's mandatory")
  String deviceModel,
  @NotNull(message = "A valid IoT device Type is mandatory")
  IotDeviceType deviceType) {

    public IoTDeviceRegisterRequest(final String deviceName,
                                    final String deviceModel,
                                    final IotDeviceType deviceType) {
        this.deviceName = normalizeSpace(deviceName);
        this.deviceModel = normalizeSpace(deviceModel);
        this.deviceType = deviceType;
    }
}
