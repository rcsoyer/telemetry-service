package org.acme.telemetryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TelemetryServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(TelemetryServiceApplication.class, args);
    }
}