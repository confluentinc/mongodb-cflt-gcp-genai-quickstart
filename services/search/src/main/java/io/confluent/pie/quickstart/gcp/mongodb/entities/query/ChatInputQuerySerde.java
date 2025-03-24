package io.confluent.pie.quickstart.gcp.mongodb.entities.query;

import org.apache.kafka.common.serialization.Serdes;

/**
 * Serde for ChatInputQuery
 */
public class ChatInputQuerySerde extends Serdes.WrapperSerde<ChatInputQuery> {
    public ChatInputQuerySerde() {
        super(new ChatInputQuerySerializer(), new ChatInputQueryDeserializer());
    }
}
