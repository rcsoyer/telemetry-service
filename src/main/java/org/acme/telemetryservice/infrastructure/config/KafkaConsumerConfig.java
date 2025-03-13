package org.acme.telemetryservice.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TYPE_MAPPINGS;

@Slf4j
@EnableKafka
@Configuration
class KafkaConsumerConfig {

    @Bean
    ConsumerFactory<String, Object> consumerFactory(final KafkaProperties properties,
                                                    final ObjectMapper objectMapper) {
        final Map<String, Object> configs = properties.buildConsumerProperties();
        configs.put(GROUP_ID_CONFIG, "telemetryGroup");
        configs.put(TRUSTED_PACKAGES, "*");
        configs.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configs.put(
          TYPE_MAPPINGS,
          "fridgeTelemetryEvent:org.acme.telemetryservice.domain.dto.command.FridgeTelemetryData,"
            + "coffeeMachineTelemetryEvent:org.acme.telemetryservice.domain.dto.command.CoffeeMachineTelemetryData,"
            + "thermostatTelemetryEvent:org.acme.telemetryservice.domain.dto.command.ThermostatTelemetryData");
        return new DefaultKafkaConsumerFactory<>(
          configs, new StringDeserializer(), new JsonDeserializer<>(objectMapper));
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
      final ConsumerFactory<String, Object> consumerFactory,
      @Qualifier("applicationTaskExecutor") final AsyncTaskExecutor virtualThreadAsyncTaskExecutor
    ) {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(consumerFactory);
        factory.setContainerCustomizer(
          customizer ->
            customizer.getContainerProperties()
                      .setListenerTaskExecutor(virtualThreadAsyncTaskExecutor)
        );
        return factory;
    }
}
