package io.confluent.pie.quickstart.gcp.mongodb.entities;

import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializerConfig;

/**
 * ChatInputQueryDeserializer is a deserializer for ChatInputQuery.
 */
public class ChatInputQueryDeserializer extends KafkaJsonSchemaDeserializer<ChatInputQuery> {
    @Override
    protected void configure(KafkaJsonSchemaDeserializerConfig config, Class<ChatInputQuery> type) {
        super.configure(config, ChatInputQuery.class);
    }
}
