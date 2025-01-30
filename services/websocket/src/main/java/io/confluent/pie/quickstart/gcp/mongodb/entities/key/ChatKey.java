package io.confluent.pie.quickstart.gcp.mongodb.entities.key;

import io.confluent.kafka.schemaregistry.annotations.Schema;

/**
 * ChatKey is a key for the chat.
 *
 * @param sessionId the session id
 */
@Schema(value = """
        {
          "properties": {
            "sessionId": {
              "connect.index": 0,
              "type": "string"
            }
          },
          "required": [
            "sessionId"
          ],
          "title": "Record",
          "type": "object"
        }""", refs = {})
public record ChatKey(String sessionId) {
}
