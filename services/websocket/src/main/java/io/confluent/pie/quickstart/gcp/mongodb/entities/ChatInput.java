package io.confluent.pie.quickstart.gcp.mongodb.entities;

import io.confluent.kafka.schemaregistry.annotations.Schema;

/**
 * ChatInput represents the input from the user.
 *
 * @param sessionId Websocket session id
 * @param userId    User id
 * @param messageId Message id
 * @param input     User input
 * @param history   Chat history
 * @param timestamp Timestamp
 */
@Schema(value = """
        {
          "properties": {
            "createdAt": {
              "connect.index": 5,
              "oneOf": [
                {
                  "type": "null"
                },
                {
                  "connect.type": "int64",
                  "title": "org.apache.kafka.connect.data.Timestamp",
                  "type": "number"
                }
              ]
            },
            "history": {
              "connect.index": 4,
              "oneOf": [
                {
                  "type": "null"
                },
                {
                  "type": "string"
                }
              ]
            },
            "input": {
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
public record ChatInput(String sessionId, String userId, String messageId, String input, String history,
                        String timestamp) {
}
