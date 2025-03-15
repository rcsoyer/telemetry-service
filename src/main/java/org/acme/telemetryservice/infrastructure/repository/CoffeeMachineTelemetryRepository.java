package org.acme.telemetryservice.infrastructure.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.acme.telemetryservice.domain.dto.query.CoffeeMachineStatusSummary;
import org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeMachineTelemetryRepository
  extends JpaRepository<CoffeeMachineTelemetryEvent, Long> {

    @Query("SELECT telemetryEvent.sourceDevice.deviceId AS deviceId, "
             + "COUNT(telemetryEvent) AS totalCount, "
             + "telemetryEvent.status AS deviceStatus "
             + "FROM CoffeeMachineTelemetryEvent telemetryEvent "
             + "WHERE telemetryEvent.sourceDevice.deviceId = :deviceId "
             + "AND telemetryEvent.status IN ('READY', 'ERROR') "
             + "AND (cast(:startDate as timestamp) is null "
             + "AND cast(:endDate as timestamp) is null) "
             + "OR (telemetryEvent.createdAt BETWEEN :startDate AND :endDate) "
             + "GROUP BY telemetryEvent.sourceDevice.deviceId, telemetryEvent.status")
    List<CoffeeMachineStatusSummary> getMachineEventsSummary(
      @Param("deviceId") UUID deviceId,
      @Param("startDate") Instant startDate,
      @Param("endDate") Instant endDate);
}
