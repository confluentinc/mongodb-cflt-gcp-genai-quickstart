package io.confluent.pie.quickstart.gcp.mongodb.kafka;

import io.confluent.pie.quickstart.gcp.mongodb.entities.ChatInput;
import io.confluent.pie.quickstart.gcp.mongodb.entities.ChatKey;
import io.confluent.pie.quickstart.gcp.mongodb.entities.ChatOutput;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfiguration {

    /**
     * Kafka template for producing messages
     *
     * @param kafkaProperties Kafka properties
     * @return Kafka template
     */
    @Bean
    public KafkaTemplate<ChatKey, ChatInput> kafkaTemplate(KafkaProperties kafkaProperties) {
        final Map<String, Object> producerProperties = kafkaProperties.buildProducerProperties();
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerProperties));
    }

    /**
     * Kafka listener container factory for consuming messages
     *
     * @param kafkaProperties Kafka properties
     * @return Kafka listener container factory
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<ChatKey, ChatOutput> kafkaListenerContainerFactory(KafkaProperties kafkaProperties) {
        ConcurrentKafkaListenerContainerFactory<ChatKey, ChatOutput> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(defaultConsumerFactory(kafkaProperties));
        return factory;
    }

    private ConsumerFactory<ChatKey, ChatOutput> defaultConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> consumerProps = kafkaProperties.buildConsumerProperties(null);
        return new DefaultKafkaConsumerFactory<>(consumerProps);
    }
}
