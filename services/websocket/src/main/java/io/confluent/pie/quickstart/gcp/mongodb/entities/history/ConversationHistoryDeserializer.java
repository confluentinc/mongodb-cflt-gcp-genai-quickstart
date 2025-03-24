package io.confluent.pie.quickstart.gcp.mongodb.entities.history;

import io.confluent.kafka.serializers.KafkaJsonDecoderConfig;
import io.confluent.kafka.serializers.KafkaJsonDeserializer;

public class ConversationHistoryDeserializer extends KafkaJsonDeserializer<ConversationHistory> {

    @Override
    protected void configure(KafkaJsonDecoderConfig config, Class<ConversationHistory> type) {
        super.configure(config, ConversationHistory.class);
    }
}
