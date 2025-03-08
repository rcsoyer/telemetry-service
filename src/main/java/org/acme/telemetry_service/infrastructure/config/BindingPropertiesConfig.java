package org.acme.telemetry_service.infrastructure.config;

import org.acme.telemetry_service.infrastructure.binding.SecurityJwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SecurityJwtProperties.class)
class BindingPropertiesConfig {
}
