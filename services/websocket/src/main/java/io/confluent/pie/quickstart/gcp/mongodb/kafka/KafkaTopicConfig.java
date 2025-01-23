package io.confluent.pie.quickstart.gcp.mongodb.kafka;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    public String chatInputTopic() {
        return "chat_input";
    }

    public String chatOutputTopic() {
        return "chat_output";
    }

}
