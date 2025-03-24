package io.confluent.pie.quickstart.gcp.mongodb.entities.query;

import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;

public class ChatInputQuerySerializer extends KafkaJsonSchemaSerializer<ChatInputQuery> {
    public ChatInputQuerySerializer() {
        super();
    }
}
