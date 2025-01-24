package io.confluent.pie.quickstart.gcp.mongodb.entities;

import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializerConfig;

public class ChatInputKeyDeserializer extends KafkaJsonSchemaDeserializer<ChatInputKey> {

    @Override
    protected void configure(KafkaJsonSchemaDeserializerConfig config, Class<ChatInputKey> type) {
        super.configure(config, ChatInputKey.class);
    }
}
