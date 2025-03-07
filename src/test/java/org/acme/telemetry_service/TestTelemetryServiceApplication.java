package org.acme.telemetry_service;

import org.springframework.boot.SpringApplication;

public class TestTelemetryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(TelemetryServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
