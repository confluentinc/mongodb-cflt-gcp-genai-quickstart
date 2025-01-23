package io.confluent.pie.quickstart.gcp.mongodb.entities;

import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializerConfig;

/**
 * ChatKeyDeserializer is a deserializer for ChatKey.
 */
public class ChatKeyDeserializer extends KafkaJsonSchemaDeserializer<ChatKey> {

    @Override
    protected void configure(KafkaJsonSchemaDeserializerConfig config, Class<ChatKey> type) {
        super.configure(config, ChatKey.class);
    }
}
