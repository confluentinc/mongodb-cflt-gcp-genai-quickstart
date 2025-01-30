package io.confluent.pie.quickstart.gcp.mongodb.entities.output;

import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializerConfig;

/**
 * Kafka deserializer for {@link ChatOutput}.
 */
public class ChatOutputDeserializer extends KafkaJsonSchemaDeserializer<ChatOutput> {

    @Override
    protected void configure(KafkaJsonSchemaDeserializerConfig config, Class<ChatOutput> type) {
        super.configure(config, ChatOutput.class);
    }
}
