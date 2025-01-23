package io.confluent.pie.quickstart.gcp.mongodb.entities;

/**
 * User message received from the websocket
 *
 * @param userId    User id
 * @param messageId Message id
 * @param message   Message
 */
public record UserMessage(String userId, String messageId, String message) {
}
