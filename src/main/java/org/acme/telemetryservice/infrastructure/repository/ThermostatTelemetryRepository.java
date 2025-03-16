package org.acme.telemetryservice.infrastructure.repository;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.acme.telemetryservice.domain.dto.query.ThermostatSummary;
import org.acme.telemetryservice.domain.entity.ThermostatTelemetryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ThermostatTelemetryRepository
  extends JpaRepository<ThermostatTelemetryEvent, Long> {

    @Query("SELECT telemetryEvent.sourceDevice.deviceId AS deviceId, "
             + "AVG(telemetryEvent.temperature) AS avgTemperature, "
             + "AVG(telemetryEvent.humidity) AS avgHumidity, "
             + "MAX(telemetryEvent.temperature) AS maxTemperature, "
             + "MIN(telemetryEvent.temperature) AS minTemperature, "
             + "MAX(telemetryEvent.humidity) AS maxHumidity, "
             + "MIN(telemetryEvent.humidity) AS minHumidity "
             + "FROM ThermostatTelemetryEvent telemetryEvent "
             + "WHERE telemetryEvent.sourceDevice.deviceId = :deviceId "
             + "AND ((cast(:startDate as timestamp) is null "
             + "OR cast(:endDate as timestamp) is null) "
             + "OR telemetryEvent.createdAt BETWEEN :startDate AND :endDate) "
             + "GROUP BY telemetryEvent.sourceDevice.deviceId")
    Optional<ThermostatSummary> getThermostatSummary(
      @Nonnull @Param("deviceId") UUID deviceId,
      @Nullable @Param("startDate") Instant startDate,
      @Nullable @Param("endDate") Instant endDate);
}
