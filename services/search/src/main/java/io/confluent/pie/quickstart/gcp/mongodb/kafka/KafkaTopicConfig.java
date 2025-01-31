package io.confluent.pie.quickstart.gcp.mongodb.kafka;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    public String getInputTopic() {
        return "chat_input_embeddings";
    }

    public String getOutputTopic() {
        return "chat_input_with_products";
    }

}
