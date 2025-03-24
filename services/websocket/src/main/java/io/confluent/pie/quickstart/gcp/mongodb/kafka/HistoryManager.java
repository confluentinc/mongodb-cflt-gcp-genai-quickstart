package io.confluent.pie.quickstart.gcp.mongodb.kafka;

import io.confluent.pie.quickstart.gcp.mongodb.entities.history.ConversationHistory;
import io.kcache.Cache;
import io.kcache.KafkaCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;


@Slf4j
@Component
public class HistoryManager implements Closeable {

    private final Cache<String, ConversationHistory> cache;

    public HistoryManager(@Autowired KafkaCache<String, ConversationHistory> cache) {
        this.cache = cache;
    }

    public void onSessionClose(String sessionId) {
        cache.remove(sessionId);
    }

    public void onHumanActivity(String sessionId, String humanQuery) {
        ConversationHistory conversationHistory = cache.get(sessionId);
        if (conversationHistory == null) {
            conversationHistory = new ConversationHistory(sessionId);
        }
        conversationHistory.addHumanQuery(humanQuery);
        cache.put(sessionId, conversationHistory);
    }

    public void onBotActivity(String sessionId, String botResponse) {
        ConversationHistory conversationHistory = cache.get(sessionId);
        if (conversationHistory == null) {
            conversationHistory = new ConversationHistory(sessionId);
        }
        conversationHistory.addBotMessage(botResponse);
        cache.put(sessionId, conversationHistory);
    }

    public String getHistory(String sessionId) {
        ConversationHistory conversationHistory = cache.get(sessionId);
        if (conversationHistory == null) {
            return "";
        }

        return cache.get(sessionId).getConversation();
    }

    @Override
    public void close() throws IOException {
        log.info("Closing history manager");
        cache.close();
    }
}

