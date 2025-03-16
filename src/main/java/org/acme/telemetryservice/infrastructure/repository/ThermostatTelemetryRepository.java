package org.acme.telemetryservice.infrastructure.repository;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.time.Instant;
import java.util.List;
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
             + "COUNT(telemetryEvent) AS totalCount "
             + "FROM ThermostatTelemetryEvent telemetryEvent "
             + "WHERE telemetryEvent.sourceDevice.deviceId = :deviceId "
             + "AND ((cast(:startDate as timestamp) is null "
             + "OR cast(:endDate as timestamp) is null) "
             + "OR telemetryEvent.createdAt BETWEEN :startDate AND :endDate)")
    List<ThermostatSummary> getMachineEventsSummary(
      @Nonnull @Param("deviceId") UUID deviceId,
      @Nullable @Param("startDate") Instant startDate,
      @Nullable @Param("endDate") Instant endDate);
}
