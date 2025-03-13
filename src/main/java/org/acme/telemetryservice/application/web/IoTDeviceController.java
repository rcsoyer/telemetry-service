package org.acme.telemetryservice.application.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.command.IoTDeviceRegisterRequest;
import org.acme.telemetryservice.domain.dto.command.IoTDeviceRegisterResponse;
import org.acme.telemetryservice.domain.service.IoTDeviceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("iot-devices")
class IoTDeviceController {

    private final IoTDeviceService service;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Register a new IoT Device",
      description = "This operation creates a new IoT Device into the system")
    @ApiResponse(responseCode = "201", description = "IoT Device successfully registered")
    @ApiResponse(responseCode = "400", description = "Invalid input data",
      content = @Content(mediaType = "application/problem+json",
        schema = @Schema(implementation = Problem.class)))
    @ApiResponse(responseCode = "409",
      description = "It already exists an IoT Device with the same name",
      content = @Content(mediaType = "application/problem+json",
        schema = @Schema(implementation = Problem.class)))
    IoTDeviceRegisterResponse register(@RequestBody @Valid final IoTDeviceRegisterRequest request) {
        log.debug("Rest API call to register a new IoT Device");
        return service.createDevice(request);
    }
}
