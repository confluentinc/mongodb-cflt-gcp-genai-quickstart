package io.confluent.pie.quickstart.gcp.mongodb.entities.data;

import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializerConfig;

public class ChatInputWithDataDeserializer extends KafkaJsonSchemaDeserializer<ChatInputWithData> {
    @Override
    protected void configure(KafkaJsonSchemaDeserializerConfig config, Class<ChatInputWithData> type) {
        super.configure(config, ChatInputWithData.class);
    }
}
