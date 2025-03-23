package org.acme.telemetryservice.infrastructure.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.convert.TypeMapper;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper.TypePrecedence;
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

    private final Validator validator;

    @Bean
    RecordMessageConverter multiTypeConverter(final ObjectMapper objectMapper) {
        final var typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.addTrustedPackages("org.acme.telemetryservice.domain.dto.command");
        //typeMapper.setUseForKey(true);
        final var messageConverter = new MultiTypeJsonMessageConverter(objectMapper);
        messageConverter.setTypeMapper(typeMapper);
        return messageConverter;
    }

    @Bean
    ConsumerFactory<String, Object> consumerFactory(
      final KafkaProperties properties,
      final ObjectMapper objectMapper) {
        final var configs = new HashMap<String, Object>(7);
        configs.put(BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        configs.put(GROUP_ID_CONFIG, "telemetryGroup");
        configs.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        configs.put(TRUSTED_PACKAGES, "org.acme.telemetryservice.domain.dto.command");
        configs.put(VALUE_DEFAULT_TYPE, "java.lang.Object");
        //  configs.put(VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configs.put(USE_TYPE_INFO_HEADERS, false);
        configs.put(REMOVE_TYPE_INFO_HEADERS, true);
        /*configs.put(
          TYPE_MAPPINGS,
          "fridgeTelemetryEvent:org.acme.telemetryservice.domain.dto.command.FridgeTelemetryData,"
            + "coffeeMachineTelemetryEvent:org.acme.telemetryservice.domain.dto.command.CoffeeMachineTelemetryData,"
            + "thermostatTelemetryEvent:org.acme.telemetryservice.domain.dto.command.ThermostatTelemetryData");
        */
        // deserializer.configure(properties.buildConsumerProperties(), false);
        final var deserializer = new JsonDeserializer<>(objectMapper);
        // deserializer.setTypeMapper(typeMapper);
        //   deserializer.setUseTypeHeaders(false);
        // deserializer.configure(configs, true);
        return new DefaultKafkaConsumerFactory<>(
          configs,
          new StringDeserializer(),
          new ErrorHandlingDeserializer<>(deserializer));
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


/*    private static class NoHeaderJsonDeserializer<T> extends JsonDeserializer<T> {

        private NoHeaderJsonDeserializer(final ObjectMapper objectMapper,
                                         final Jackson2JavaTypeMapper typeMapper) {
            super((Class<T>) null, objectMapper, false);
            setRemoveTypeHeaders(true);
            setUseTypeHeaders(false);
            setUseTypeMapperForKey(false);
            setTypeMapper(typeMapper);
        }
    }*/

    private static class MultiTypeJsonMessageConverter extends ByteArrayJsonMessageConverter {

        private MultiTypeJsonMessageConverter(final ObjectMapper objectMapper) {
            super(objectMapper);
        }

        @Override
  /*      @SneakyThrows({JsonMappingException.class, JsonProcessingException.class,
          IOException.class})*/
        protected Object extractAndConvertValue(final ConsumerRecord<?, ?> record,
                                                final Type type) {
            final JavaType javaType = determineJavaType(record, type);
         //   final var recordValue = (LinkedHashMap<?, ?>) record.value();

            return getObjectMapper().convertValue(record.value(), javaType);
        }

        private JavaType determineJavaType(ConsumerRecord<?, ?> record, Type type) {
            JavaType javaType = getTypeMapper().getTypePrecedence().equals(TypePrecedence.INFERRED) && type != null
                                  ? TypeFactory.defaultInstance().constructType(type)
                                  : getTypeMapper().toJavaType(record.headers());
            if (javaType == null) { // no headers
                if (type != null) {
                    javaType = TypeFactory.defaultInstance().constructType(type);
                }
            }
            return javaType;
        }
    }
}
