package io.confluent.pie.quickstart.gcp.mongodb.entities.output;

import org.apache.kafka.common.serialization.Serdes;

public class ChatOutputSerde extends Serdes.WrapperSerde<ChatOutput> {
    public ChatOutputSerde() {
        super(new ChatOutputSerializer(), new ChatOutputDeserializer());
    }
}
