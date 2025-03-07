package org.acme.telemetry_service.intrastructure.config;

import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@EnableKafka
@Configuration
class KafkaConsumerConfig {

    @Bean
    ConsumerFactory<String, String> consumerFactory() {
        final Map<String, Object> configs =
          Map.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                 "bootstrapAddress",
                 ConsumerConfig.GROUP_ID_CONFIG,
                 "groupId",
                 ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                 StringDeserializer.class,
                 ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                 StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String>
    kafkaListenerContainerFactory() {
        final var factory =
          new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
