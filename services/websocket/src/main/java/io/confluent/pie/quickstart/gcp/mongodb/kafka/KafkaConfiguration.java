package io.confluent.pie.quickstart.gcp.mongodb.kafka;

import io.confluent.pie.quickstart.gcp.mongodb.entities.history.ConversationHistory;
import io.confluent.pie.quickstart.gcp.mongodb.entities.history.ConversationHistorySerde;
import io.confluent.pie.quickstart.gcp.mongodb.entities.input.ChatInput;
import io.confluent.pie.quickstart.gcp.mongodb.entities.key.ChatKey;
import io.confluent.pie.quickstart.gcp.mongodb.entities.output.ChatOutput;
import io.kcache.KafkaCache;
import io.kcache.KafkaCacheConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

import static io.kcache.KafkaCacheConfig.KAFKACACHE_CLIENT_ID_CONFIG;
import static io.kcache.KafkaCacheConfig.KAFKACACHE_TOPIC_CONFIG;

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

    @Bean
    public KafkaCache<String, ConversationHistory> kafkaCache(KafkaProperties kafkaProperties) {
        // To KafkaCache properties
        final Map<String, Object> producerProperties = kafkaProperties.buildProducerProperties();
        final Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties(null);

        // Create KafkaCache properties
        final Map<String, Object> kafkaCacheProperties = new HashMap<>();
        consumerProperties.forEach((k, v) -> kafkaCacheProperties.put("kafkacache." + k, v));
        producerProperties.forEach((k, v) -> kafkaCacheProperties.put("kafkacache." + k, v));
        kafkaCacheProperties.put(KAFKACACHE_TOPIC_CONFIG, "conversation-history");
        kafkaCacheProperties.put(KAFKACACHE_CLIENT_ID_CONFIG, consumerProperties.get("client.id") + "|kafkacache");

        // Clear unnecessary properties
        kafkaCacheProperties.remove("kafkacache.key.serializer");
        kafkaCacheProperties.remove("kafkacache.value.serializer");
        kafkaCacheProperties.remove("kafkacache.key.deserializer");
        kafkaCacheProperties.remove("kafkacache.value.deserializer");

        //
        final ConversationHistorySerde valueSerde = new ConversationHistorySerde();
        valueSerde.configure(producerProperties, false);
        final Serdes.StringSerde keySerde = new Serdes.StringSerde();
        keySerde.configure(producerProperties, true);

        final KafkaCache<String, ConversationHistory> cache = new KafkaCache<>(
                new KafkaCacheConfig(kafkaCacheProperties),
                keySerde,
                valueSerde);
        cache.init();

        return cache;
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
