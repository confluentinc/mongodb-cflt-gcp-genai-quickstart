package io.confluent.pie.quickstart.gcp.mongodb.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.pie.quickstart.gcp.mongodb.entities.UserMessage;
import io.confluent.pie.quickstart.gcp.mongodb.kafka.ChatInputHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * WebSockerHandler
 */
@Slf4j
@Component
public class WebSockerHandler extends TextWebSocketHandler {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final ChatInputHandler chatInputHandler;

    public WebSockerHandler(@Autowired ChatInputHandler chatInputHandler) {
        this.chatInputHandler = chatInputHandler;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connection established: {}", session.getId());

        // Handle new session
        chatInputHandler.onNewSession(session);

        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        // Handle session close
        chatInputHandler.onSessionClose(session);

        log.info("Connection closed: {}", session.getId());
    }

    /**
     * Handle text message
     *
     * @param session Websocket session
     * @param message Text message
     * @throws Exception Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final UserMessage userMessage = OBJECT_MAPPER.readValue(message.getPayload(), UserMessage.class);
        log.info("Received message from {}: {}", session.getId(), userMessage);

        chatInputHandler.onNewMessage(session, userMessage);
    }
}
