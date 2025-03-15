package org.acme.telemetryservice.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;
import org.acme.telemetryservice.domain.dto.query.CoffeeMachineTotalCoffeesMade;
import org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeMachineTelemetryRepository
  extends JpaRepository<CoffeeMachineTelemetryEvent, Long> {

    @Query("SELECT telemetryEvent.sourceDevice.deviceId AS deviceId, "
             + "COUNT(telemetryEvent) AS allTimeTotalCoffeesMade "
             + "FROM CoffeeMachineTelemetryEvent telemetryEvent "
             + "WHERE telemetryEvent.sourceDevice.deviceId = ?1 "
             + "AND telemetryEvent.status = 'READY'")
    Optional<CoffeeMachineTotalCoffeesMade> countTotalCoffeesMade(UUID deviceId);
}
