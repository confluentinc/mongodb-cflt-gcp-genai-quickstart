package io.confluent.pie.quickstart.gcp.mongodb.entities;

public record Metadata(
        String input,
        String userId,
        String messageId,
        String history) {
}