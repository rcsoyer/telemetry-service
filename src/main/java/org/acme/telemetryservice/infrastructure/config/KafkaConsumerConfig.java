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
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
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
    RecordMessageConverter multiTypeConverter(final ObjectMapper objectMapper) {
        final var typeMapper = new DefaultJackson2JavaTypeMapper();
        //     typeMapper.setTypePrecedence(TYPE_ID);
        typeMapper.addTrustedPackages("*");
        //    typeMapper.setIdClassMapping(Map.of("fridgeTelemetryEvent", FridgeTelemetryEvent.class));
        final var messageConverter = new KafkaJsonConverter(objectMapper);
        messageConverter.setTypeMapper(typeMapper);
        return messageConverter;
    }

    @Bean
    ConsumerFactory<String, Object> consumerFactory(final KafkaProperties properties,
                                                    final ObjectMapper objectMapper) {
        final Map<String, Object> allConfigs = properties.buildConsumerProperties();
        allConfigs.put(GROUP_ID_CONFIG, "telemetryGroup");
        allConfigs.put(TRUSTED_PACKAGES, "*");
        allConfigs.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
/*        allConfigs.put(VALUE_DEFAULT_TYPE,
                       "org.acme.telemetryservice.domain.dto.command.FridgeTelemetryEvent");*/
        // allConfigs.put(VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        allConfigs.put(
          TYPE_MAPPINGS,
          "fridgeTelemetryEvent:org.acme.telemetryservice.domain.dto.command.FridgeTelemetryEvent");
        return new DefaultKafkaConsumerFactory<>(
          allConfigs, new StringDeserializer(), new JsonDeserializer<>(objectMapper));
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
      final ConsumerFactory<String, Object> consumerFactory,
      @Qualifier("applicationTaskExecutor") final AsyncTaskExecutor virtualThreadAsyncTaskExecutor,
      final RecordMessageConverter multiTypeConverter
    ) {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setRecordMessageConverter(multiTypeConverter);
        factory.setConsumerFactory(consumerFactory);
        factory.setContainerCustomizer(
          customizer ->
            customizer.getContainerProperties()
                      .setListenerTaskExecutor(virtualThreadAsyncTaskExecutor));
        return factory;
    }
}
