package io.confluent.pie.quickstart.gcp.mongodb.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.pie.quickstart.gcp.mongodb.entities.ChatInput;
import io.confluent.pie.quickstart.gcp.mongodb.entities.ChatKey;
import io.confluent.pie.quickstart.gcp.mongodb.entities.ChatOutput;
import io.confluent.pie.quickstart.gcp.mongodb.entities.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ChatInputHandler {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final KafkaTemplate<ChatKey, ChatInput> kafkaTemplate;
    private final KafkaTopicConfig kafkaTopicConfig;

    public ChatInputHandler(@Autowired KafkaTemplate<ChatKey, ChatInput> kafkaTemplate,
                            @Autowired KafkaTopicConfig kafkaTopicConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopicConfig = kafkaTopicConfig;
    }

    /**
     * Handle new session
     *
     * @param session Websocket session
     */
    public void onNewSession(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }

    /**
     * Handle session close
     *
     * @param session Websocket session
     */
    public void onSessionClose(WebSocketSession session) {
        sessions.remove(session.getId());
    }

    public void onNewMessage(WebSocketSession session, UserMessage userMessage) {
        final ChatInput chatInput = new ChatInput(
                session.getId(),
                userMessage.userId(),
                userMessage.messageId(),
                userMessage.message(),
                "",
                String.valueOf(System.currentTimeMillis()));
        final ChatKey chatInputKey = new ChatKey(chatInput.sessionId());


        final ProducerRecord<ChatKey, ChatInput> producerRecord = new ProducerRecord<>(kafkaTopicConfig.chatInputTopic(), chatInputKey, chatInput);

        kafkaTemplate.send(producerRecord).whenComplete((recordMetadata, throwable) -> {
            if (throwable != null) {
                log.error("Failed to send message to Confluent Cloud", throwable);
            }
        });
    }

    /**
     * Handle chat output
     *
     * @param value Chat output
     */
    @KafkaListener(topics = "#{kafkaTopicConfig.chatOutputTopic()}", containerFactory = "kafkaListenerContainerFactory", groupId = "${spring.kafka.consumer.group-id}")
    public void onChatOutput(ChatOutput value) {
        if (!sessions.containsKey(value.sessionId())) {
            log.error("Session not found for session id {}", value.sessionId());
            return;
        }

        final UserMessage response = new UserMessage(
                value.userId(),
                value.messageId(),
                "Hello " + value.output());

        try {
            sessions.get(value.sessionId()).sendMessage(new TextMessage(OBJECT_MAPPER.writeValueAsString(response)));
        } catch (IOException e) {
            log.error("Failed to send message to UI", e);
        }
    }

}
