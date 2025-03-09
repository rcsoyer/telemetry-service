package org.acme.telemetryservice.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.converter.BytesJsonMessageConverter;

/**
 * Custom converter to bypass limitation on the framework default implementation
 */
class KafkaJsonConverter extends BytesJsonMessageConverter {

    KafkaJsonConverter(final ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected Object extractAndConvertValue(final ConsumerRecord<?, ?> record, final Type type) {
        try {
            return super.extractAndConvertValue(record, type);
        } catch (final IllegalStateException unrecognizedTypeError) {
            return record.value();
        }
    }
}
