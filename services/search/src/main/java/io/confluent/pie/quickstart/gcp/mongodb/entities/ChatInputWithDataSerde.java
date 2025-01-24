package io.confluent.pie.quickstart.gcp.mongodb.entities;

import org.apache.kafka.common.serialization.Serdes;

public class ChatInputWithDataSerde extends Serdes.WrapperSerde<ChatInputWithData> {

    public ChatInputWithDataSerde() {
        super(new ChatInputWithDataSerializer(), new ChatInputWithDataDeserializer());
    }
}
