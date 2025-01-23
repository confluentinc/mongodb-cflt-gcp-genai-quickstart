package io.confluent.pie.quickstart.gcp.mongodb.entities;

import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;

/**
 * Serializer for ChatKey
 */
public class ChatKeySerializer extends KafkaJsonSchemaSerializer<ChatKey> {
    public ChatKeySerializer() {
    }
}
