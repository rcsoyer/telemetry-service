package org.acme.telemetryservice.infrastructure.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.validation.Validator;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.springframework.kafka.support.serializer.JsonDeserializer.REMOVE_TYPE_INFO_HEADERS;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES;
import static org.springframework.kafka.support.serializer.JsonDeserializer.USE_TYPE_INFO_HEADERS;
import static org.springframework.kafka.support.serializer.JsonDeserializer.VALUE_DEFAULT_TYPE;

@Slf4j
@EnableKafka
@Configuration
@RequiredArgsConstructor
class KafkaConsumerConfig implements KafkaListenerConfigurer {

    private static final String EVENTS_PACKAGE = "org.acme.telemetryservice.domain.dto.command";

    private final Validator validator;

    /**
     * MultiType implementation that overrides the type limitations of the super class:
     * {@link JsonMessageConverter#extractAndConvertValue(ConsumerRecord, Type)}.
     * <p/> With this simple custom implementation, the topic listener methods,
     * can properly convert its parameters.
     */
    @Bean
    RecordMessageConverter multiTypeConverter(final ObjectMapper objectMapper) {
        return new JsonMessageConverter(objectMapper) {
            {
                getTypeMapper().addTrustedPackages(EVENTS_PACKAGE);
            }

            @Override
            protected Object extractAndConvertValue(final ConsumerRecord<?, ?> record,
                                                    final Type type) {
                final JavaType javaType = getObjectMapper().constructType(type);
                return getObjectMapper().convertValue(record.value(), javaType);
            }
        };
    }

    @Bean
    ConsumerFactory<String, Object> consumerFactory(
      final KafkaProperties properties,
      final ObjectMapper objectMapper) {
        final var configs = new HashMap<String, Object>(7);
        configs.put(BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        configs.put(GROUP_ID_CONFIG, "telemetryGroup");
        configs.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        configs.put(TRUSTED_PACKAGES, EVENTS_PACKAGE);
        configs.put(VALUE_DEFAULT_TYPE, "java.lang.Object");
        configs.put(USE_TYPE_INFO_HEADERS, false);
        configs.put(REMOVE_TYPE_INFO_HEADERS, true);

        final var deserializer = new JsonDeserializer<>(objectMapper);
        return new DefaultKafkaConsumerFactory<>(
          configs,
          new StringDeserializer(),
          new ErrorHandlingDeserializer<>(deserializer)
        );
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
      final ConsumerFactory<String, Object> consumerFactory,
      @Qualifier("applicationTaskExecutor") final AsyncTaskExecutor virtualThreadAsyncTaskExecutor,
      final RecordMessageConverter multiTypeConverter) {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(consumerFactory);
        factory.setRecordMessageConverter(multiTypeConverter);
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