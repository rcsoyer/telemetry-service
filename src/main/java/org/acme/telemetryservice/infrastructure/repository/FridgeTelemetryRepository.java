package org.acme.telemetryservice.infrastructure.repository;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.acme.telemetryservice.domain.dto.query.FridgeSummary;
import org.acme.telemetryservice.domain.entity.FridgeTelemetryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FridgeTelemetryRepository extends JpaRepository<FridgeTelemetryEvent, Long> {

    @Query("SELECT telemetryEvent.sourceDevice.deviceId AS deviceId, "
             + "AVG(telemetryEvent.temperature) AS avgTemperature, "
             + "MAX(telemetryEvent.temperature) AS maxTemperature, "
             + "MIN(telemetryEvent.temperature) AS minTemperature, "
             + "PERCENTILE_CONT(0.5) WITHIN GROUP "
             + "(ORDER BY telemetryEvent.temperature) AS medianTemperature "
             + "FROM FridgeTelemetryEvent telemetryEvent "
             + "WHERE telemetryEvent.sourceDevice.deviceId = :deviceId "
             + "AND ((cast(:startDate as timestamp) is null "
             + "OR cast(:endDate as timestamp) is null) "
             + "OR telemetryEvent.createdAt BETWEEN :startDate AND :endDate) "
             + "GROUP BY telemetryEvent.sourceDevice.deviceId")
    Optional<FridgeSummary> getFridgeSummary(
      @Nonnull @Param("deviceId") UUID deviceId,
      @Nullable @Param("startDate") Instant startDate,
      @Nullable @Param("endDate") Instant endDate);
}
