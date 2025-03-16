package org.acme.telemetryservice.application.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.query.FridgeSummary;
import org.acme.telemetryservice.domain.dto.query.TelemetryEventFilter;
import org.acme.telemetryservice.domain.service.FridgeTelemetryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("fridges")
class FridgeController {

    private final FridgeTelemetryService service;

    @GetMapping("{deviceId}")
    @Operation(summary = "View summary data of Fridge events",
      description = "Check in the system information about historical events of a IoT Device")
    @ApiResponse(responseCode = "200",
      description = "The query parameters were valid and successfully got the data")
    @ApiResponse(responseCode = "404", description = "No data found matching the filters",
      content = @Content(mediaType = "application/problem+json",
        schema = @Schema(implementation = Problem.class)))
    FridgeSummary getSummaryBy(@Valid final TelemetryEventFilter filter) {
        log.debug("REST API call to get summary information about a fridge. filter={}", filter);
        return service.summarize(filter);
    }
}
