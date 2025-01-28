package io.confluent.pie.quickstart.gcp.mongodb.kafka;

import io.confluent.pie.quickstart.gcp.mongodb.entities.key.ChatInputKeySerde;
import io.confluent.pie.quickstart.gcp.mongodb.entities.query.ChatInputQuerySerde;
import io.confluent.pie.quickstart.gcp.mongodb.entities.data.ChatInputWithDataSerde;
import io.confluent.pie.quickstart.gcp.mongodb.repository.ProductRepo;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SearchTopology {

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder, KafkaProperties kafkaProperties, ProductRepo productRepo) {
        final Map<String, ?> properties = kafkaProperties.buildProducerProperties();
        final ChatInputKeySerde chatInputKeySerde = new ChatInputKeySerde();
        final ChatInputQuerySerde chatInputQuerySerde = new ChatInputQuerySerde();
        final ChatInputWithDataSerde chatInputWithDataSerde = new ChatInputWithDataSerde();

        chatInputKeySerde.configure(properties, true);
        chatInputQuerySerde.configure(properties, false);
        chatInputWithDataSerde.configure(properties, false);

        streamsBuilder
                .stream("chat_input_embeddings", Consumed.with(chatInputKeySerde, chatInputQuerySerde))
                .process(() -> new ChatInputProcessor(productRepo))
                .to("chat_input_with_products", Produced.with(chatInputKeySerde, chatInputWithDataSerde));


    }

}
