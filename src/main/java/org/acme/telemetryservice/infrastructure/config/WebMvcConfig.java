package org.acme.telemetryservice.infrastructure.config;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nullable;
import javax.crypto.SecretKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.DefaultFormattingConversionService;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Configuration
class WebMvcConfig {

    @Bean
    Converter<String, SecretKey> secretKeyConverter() {
        return new Converter<>() {
            @Override
            public SecretKey convert(@Nullable final String source) {
                return isNotBlank(source) ? Keys.hmacShaKeyFor(source.getBytes()) : null;
            }
        };
    }

    @Bean
    ConversionService conversionService() {
        final var conversionService = new DefaultFormattingConversionService();
        conversionService.addConverter(secretKeyConverter());
        return conversionService;
    }

}
