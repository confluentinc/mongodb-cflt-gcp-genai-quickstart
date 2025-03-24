package io.confluent.pie.quickstart.gcp.mongodb.entities.history;

import org.apache.kafka.common.serialization.Serdes;

public class ConversationHistorySerde extends Serdes.WrapperSerde<ConversationHistory> {

    public ConversationHistorySerde() {
        super(new ConversationHistorySerializer(),
                new ConversationHistoryDeserializer());
    }
}
