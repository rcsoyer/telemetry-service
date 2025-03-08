package org.acme.telemetry_service.intrastructure.config;

import java.util.Map;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper.TypePrecedence.INFERRED;

@EnableKafka
@Configuration
class KafkaConsumerConfig {

    @Bean
    RecordMessageConverter multiTypeConverter() {
        final var typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(INFERRED);
        typeMapper.addTrustedPackages("org.acme.telemetry_service.domain.dto.command");
        final var messageConverter = new StringJsonMessageConverter();
        messageConverter.setTypeMapper(typeMapper);
        return messageConverter;
    }

    @Bean
    ConsumerFactory<String, Object> consumerFactory(final KafkaProperties properties) {
        final Map<String, Object> configs =
          Map.of(BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers(),
                 KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                 VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
      final ConsumerFactory<String, Object> consumerFactory,
      final AsyncTaskExecutor virtualThreadAsyncTaskExecutor,
      final RecordMessageConverter multiTypeConverter) {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(consumerFactory);
        factory.setRecordMessageConverter(multiTypeConverter);
        factory.setContainerCustomizer(
          customizer -> customizer.getContainerProperties()
                                  .setListenerTaskExecutor(virtualThreadAsyncTaskExecutor));
        return factory;
    }
}
