package io.confluent.pie.quickstart.gcp.mongodb.entities.history;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConversationHistory {

    private String sessionId;
    private List<String> messages = new ArrayList<>();

    public ConversationHistory(String sessionId) {
        this.sessionId = sessionId;
    }

    public void addHumanQuery(String message) {
        messages.add("Human: " + message);
    }

    public void addBotMessage(String message) {
        messages.add("Assistant: " + message);
    }

    @JsonIgnore
    public String getConversation() {
        return String.join("\n", messages);
    }
}
