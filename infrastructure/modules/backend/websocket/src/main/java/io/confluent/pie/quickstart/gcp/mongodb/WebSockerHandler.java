package io.confluent.pie.quickstart.gcp.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.pie.quickstart.gcp.mongodb.entities.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class WebSockerHandler extends TextWebSocketHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final UserMessage userMessage = objectMapper.readValue(message.getPayload(), UserMessage.class);
        final UserMessage response = new UserMessage(userMessage.messageId(), "Hello, " + userMessage.text());

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
    }
}
