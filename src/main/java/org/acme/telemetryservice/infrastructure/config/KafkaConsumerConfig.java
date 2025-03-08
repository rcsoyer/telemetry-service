package org.acme.telemetryservice.infrastructure.config;

import java.util.Map;
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
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.springframework.kafka.support.serializer.ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS;
import static org.springframework.kafka.support.serializer.ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES;

@EnableKafka
@Configuration
class KafkaConsumerConfig {

/*    @Bean
    RecordMessageConverter multiTypeConverter() {
        final var typeMapper = new DefaultJackson2JavaTypeMapper();
        //typeMapper.setTypePrecedence(INFERRED);
        typeMapper.addTrustedPackages("*");
        final var messageConverter = new StringJsonMessageConverter();
        messageConverter.setTypeMapper(typeMapper);
        return messageConverter;
    }*/

    @Bean
    ConsumerFactory<String, Object> consumerFactory(
      final KafkaProperties properties) {
        final Map<String, Object> allConfigs = properties.buildConsumerProperties();
        allConfigs.put(BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        allConfigs.put(KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        allConfigs.put(VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        allConfigs.put(KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        allConfigs.put(VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        allConfigs.put(GROUP_ID_CONFIG, "telemetryGroup");
        allConfigs.put(TRUSTED_PACKAGES, "*");
        // allConfigs.put(USE_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaConsumerFactory<>(
          allConfigs/*,
          new StringDeserializer(),
          new JsonDeserializer<>(objectMapper)*/);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
      final ConsumerFactory<String, Object> consumerFactory,
      @Qualifier("applicationTaskExecutor") final AsyncTaskExecutor virtualThreadAsyncTaskExecutor
      /*final RecordMessageConverter multiTypeConverter*/) {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(consumerFactory);
        //factory.setRecordMessageConverter(multiTypeConverter);
        factory.setContainerCustomizer(
          customizer -> customizer.getContainerProperties()
                                  .setListenerTaskExecutor(virtualThreadAsyncTaskExecutor));
        return factory;
    }
}
