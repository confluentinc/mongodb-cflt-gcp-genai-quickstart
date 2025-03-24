package io.confluent.pie.quickstart.gcp.mongodb.entities.input;


import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;

/**
 * ChatKeySerializer is a serializer for ChatKey.
 */
public class ChatInputSerializer extends KafkaJsonSchemaSerializer<ChatInput> {
    public ChatInputSerializer() {
    }
}
