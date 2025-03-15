package org.acme.telemetryservice.infrastructure.repository;

import java.util.UUID;
import org.acme.telemetryservice.domain.dto.query.CoffeeMachineTotalCoffeesMade;
import org.acme.telemetryservice.domain.entity.CoffeeMachineTelemetryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeMachineTelemetryRepository
  extends JpaRepository<CoffeeMachineTelemetryEvent, Long> {

    @Query("SELECT machine.sourceDevice.deviceId AS deviceId, "
             + "COUNT(machine) AS allTimeTotalCoffeesMade "
             + "FROM CoffeeMachineTelemetryEvent machine "
             + "WHERE machine.sourceDevice.deviceId = ?1 "
             + "AND machine.status = 'READY'")
    CoffeeMachineTotalCoffeesMade countTotalCoffeesMade(UUID deviceId);
}
