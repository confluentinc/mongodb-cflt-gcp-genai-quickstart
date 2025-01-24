package io.confluent.pie.quickstart.gcp.mongodb.entities;

import org.apache.kafka.common.serialization.Serdes;

/**
 * Serde for {@link ChatKey}.
 */
public class ChatKeySerde extends Serdes.WrapperSerde<ChatKey> {

    public ChatKeySerde() {
        super(new ChatKeySerializer(), new ChatKeyDeserializer());
    }
}
