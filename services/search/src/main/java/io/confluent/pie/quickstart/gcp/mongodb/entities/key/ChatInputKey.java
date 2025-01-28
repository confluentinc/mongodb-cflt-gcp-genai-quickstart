package io.confluent.pie.quickstart.gcp.mongodb.entities.key;

import io.confluent.kafka.schemaregistry.annotations.Schema;

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
        }
        """,
        refs = {}
)
public record ChatInputKey(String sessionId) {
}
