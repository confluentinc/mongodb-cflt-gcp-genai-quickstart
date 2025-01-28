package io.confluent.pie.quickstart.gcp.mongodb.entities.key;

import org.apache.kafka.common.serialization.Serdes;

public class ChatInputKeySerde extends Serdes.WrapperSerde<ChatInputKey> {
    public ChatInputKeySerde() {
        super(new ChatInputKeySerializer(), new ChatInputKeyDeserializer());
    }
}
