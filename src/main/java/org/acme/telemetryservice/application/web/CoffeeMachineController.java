package org.acme.telemetryservice.application.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.telemetryservice.domain.dto.query.CoffeeMachineStatusSummary;
import org.acme.telemetryservice.domain.dto.query.CoffeeMachineTelemetryEventFilter;
import org.acme.telemetryservice.domain.service.CoffeeMachineTelemetryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("coffee-machines")
class CoffeeMachineController {

    private final CoffeeMachineTelemetryService service;

    @GetMapping("{deviceId}")
    @Operation(summary = "Find out how many coffee were made",
      description = "Check in the system how coffees were made by a specific machine")
    @ApiResponse(responseCode = "200",
      description = "The query parameters were valid and successfully got the data")
    @ApiResponse(responseCode = "400", description = "Invalid input data",
      content = @Content(mediaType = "application/problem+json",
        schema = @Schema(implementation = Problem.class)))
    List<CoffeeMachineStatusSummary> getTotalCoffeesMade(
      @Valid final CoffeeMachineTelemetryEventFilter filter) {
        log.debug("REST API call to get summary information about a coffee machine. filter={}",
                  filter);
        return service.summarize(filter);
    }
}
