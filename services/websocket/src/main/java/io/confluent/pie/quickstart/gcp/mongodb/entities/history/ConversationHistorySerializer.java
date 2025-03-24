package io.confluent.pie.quickstart.gcp.mongodb.entities.history;

import io.confluent.kafka.serializers.KafkaJsonSerializer;

public class ConversationHistorySerializer extends KafkaJsonSerializer<ConversationHistory> {
    public ConversationHistorySerializer() {
    }
}
