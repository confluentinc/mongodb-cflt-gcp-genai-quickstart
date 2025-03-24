package io.confluent.pie.quickstart.gcp.mongodb.entities.output;

import io.confluent.kafka.schemaregistry.annotations.Schema;

/**
 * ChatOutput represents the output of a chat message.
 *
 * @param sessionId the session id
 * @param userId    user id
 * @param output    the output
 */
@Schema(value = """
        {
          "properties": {
            "messageId": {
              "connect.index": 2,
              "oneOf": [
                {
                  "type": "null"
                },
                {
                  "type": "string"
                }
              ]
            },
            "output": {
              "connect.index": 3,
              "oneOf": [
                {
                  "type": "null"
                },
                {
                  "type": "string"
                }
              ]
            },
            "sessionId": {
              "connect.index": 0,
              "type": "string"
            },
            "userId": {
              "connect.index": 1,
              "oneOf": [
                {
                  "type": "null"
                },
                {
                  "type": "string"
                }
              ]
            }
          },
          "required": [
            "sessionId"
          ],
          "title": "Record",
          "type": "object"
        }""", refs = {})
public record ChatOutput(String sessionId, String messageId, String userId, String output) {
}
