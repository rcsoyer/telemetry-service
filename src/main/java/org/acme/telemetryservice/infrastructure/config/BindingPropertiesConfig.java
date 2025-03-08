package org.acme.telemetryservice.infrastructure.config;

import org.acme.telemetryservice.infrastructure.binding.SecurityJwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SecurityJwtProperties.class)
class BindingPropertiesConfig {
}
