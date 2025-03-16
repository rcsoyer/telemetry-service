package org.acme.telemetryservice.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.validation.Validator;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TYPE_MAPPINGS;

@Slf4j
@EnableKafka
@Configuration
@RequiredArgsConstructor
class KafkaConsumerConfig implements KafkaListenerConfigurer {

    private final Validator validator;

    @Bean
    ConsumerFactory<String, Object> consumerFactory(final KafkaProperties properties,
                                                    final ObjectMapper objectMapper) {
        final var configs = new HashMap<String, Object>(6);
        configs.put(BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        configs.put(GROUP_ID_CONFIG, "telemetryGroup");
        configs.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        configs.put(TRUSTED_PACKAGES, "*");
        configs.put(VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
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

    @Override
    public void configureKafkaListeners(final KafkaListenerEndpointRegistrar registrar) {
        registrar.setValidator(validator);
    }
}
