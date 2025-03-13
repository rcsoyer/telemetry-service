package org.acme.telemetryservice.infrastructure.repository;

import org.acme.telemetryservice.domain.entity.IoTDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IoTDeviceRepository extends JpaRepository<IoTDevice, Long> {
}
