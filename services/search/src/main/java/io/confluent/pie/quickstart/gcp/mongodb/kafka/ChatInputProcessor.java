package io.confluent.pie.quickstart.gcp.mongodb.kafka;

import io.confluent.pie.quickstart.gcp.mongodb.entities.data.Medication;
import io.confluent.pie.quickstart.gcp.mongodb.entities.key.ChatInputKey;
import io.confluent.pie.quickstart.gcp.mongodb.entities.query.ChatInputQuery;
import io.confluent.pie.quickstart.gcp.mongodb.entities.data.ChatInputWithData;
import io.confluent.pie.quickstart.gcp.mongodb.repository.MedicationRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ChatInputProcessor {
    private final MedicationRepo medicationRepo;
    private final KafkaTemplate<ChatInputKey, ChatInputWithData> kafkaTemplate;
    private final KafkaTopicConfig kafkaTopicConfig;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public ChatInputProcessor(@Autowired MedicationRepo medicationRepo,
                              @Autowired KafkaTemplate<ChatInputKey, ChatInputWithData> kafkaTemplate,
                              @Autowired KafkaTopicConfig kafkaTopicConfig) {
        this.medicationRepo = medicationRepo;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopicConfig = kafkaTopicConfig;
    }

    @KafkaListener(topics = "#{kafkaTopicConfig.getInputTopic()}",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "${spring.kafka.consumer.group-id}",
            clientIdPrefix = "${spring.kafka.consumer.client-id}")
    public void onEvent(ChatInputQuery query) {
        log.info("Processing record: {}", query.sessionId());

        executorService.submit(() -> {
            final List<Medication> products = medicationRepo.findMedicationsByVector(
                    query.embeddings(),
                    query.numberOfCandidate(),
                    query.limit(),
                    query.minScore());

            final ChatInputWithData chatInputWithData = new ChatInputWithData(
                    query.sessionId(),
                    products.stream().toList(),
                    products.stream().map(Medication::getSummary).collect(Collectors.joining("\n")),
                    query.metadata()
            );

            final ProducerRecord<ChatInputKey, ChatInputWithData> producerRecord = new ProducerRecord<>(
                    kafkaTopicConfig.getOutputTopic(),
                    new ChatInputKey(query.sessionId()),
                    chatInputWithData);

            kafkaTemplate.send(producerRecord).whenComplete((recordMetadata, throwable) -> {
                if (throwable != null) {
                    log.error("Failed to forward record: {}", chatInputWithData.sessionId(), throwable);
                } else {
                    log.info("Forwarded record: {}", chatInputWithData.sessionId());
                }
            });
        });
    }
}
