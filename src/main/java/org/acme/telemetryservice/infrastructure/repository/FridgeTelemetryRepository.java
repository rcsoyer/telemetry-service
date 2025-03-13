package org.acme.telemetryservice.infrastructure.repository;

import org.acme.telemetryservice.domain.entity.FridgeTelemetryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FridgeTelemetryRepository extends JpaRepository<FridgeTelemetryEvent, Long> {
}
