package io.confluent.pie.quickstart.gcp.mongodb.entities;

import io.confluent.kafka.schemaregistry.annotations.Schema;

@Schema(value = """
        {
          "properties": {
            "requestId": {
              "connect.index": 0,
              "type": "string"
            }
          },
          "required": [
            "requestId"
          ],
          "title": "Record",
          "type": "object"
        }
        """,
        refs = {}
)
public record ChatInputKey(String requestId) {
}
