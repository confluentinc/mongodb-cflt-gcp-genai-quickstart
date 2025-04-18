package io.confluent.pie.quickstart.gcp.mongodb.entities.query;

import io.confluent.kafka.schemaregistry.annotations.Schema;
import io.confluent.pie.quickstart.gcp.mongodb.entities.Metadata;

import java.util.List;

@Schema(value = """
        {
          "properties": {
            "embeddings": {
              "connect.index": 1,
              "oneOf": [
                {
                  "type": "null"
                },
                {
                  "items": {
                    "oneOf": [
                      {
                        "type": "null"
                      },
                      {
                        "connect.type": "float32",
                        "type": "number"
                      }
                    ]
                  },
                  "type": "array"
                }
              ]
            },
            "limit": {
              "connect.index": 3,
              "oneOf": [
                {
                  "type": "null"
                },
                {
                  "connect.type": "int32",
                  "type": "number"
                }
              ]
            },
            "metadata": {
              "connect.index": 5,
              "oneOf": [
                {
                  "type": "null"
                },
                {
                  "properties": {
                    "history": {
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
                    "input": {
                      "connect.index": 0,
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
                  "title": "Record_metadata",
                  "type": "object"
                }
              ]
            },
            "minScore": {
              "connect.index": 4,
              "oneOf": [
                {
                  "type": "null"
                },
                {
                  "connect.type": "float64",
                  "type": "number"
                }
              ]
            },
            "numberOfCandidate": {
              "connect.index": 2,
              "oneOf": [
                {
                  "type": "null"
                },
                {
                  "connect.type": "int32",
                  "type": "number"
                }
              ]
            },
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
public record ChatInputQuery(String sessionId,
                             List<Double> embeddings,
                             int numberOfCandidate,
                             int limit,
                             double minScore,
                             Metadata metadata) {
    public ChatInputQuery(String sessionId, List<Double> embeddings, int numberOfCandidate, int limit, double minScore) {
        this(sessionId, embeddings, numberOfCandidate, limit, minScore, null);
    }

    public ChatInputQuery(String sessionId, List<Double> embeddings, int numberOfCandidate, int limit, double minScore, Metadata metadata) {
        this.sessionId = sessionId;
        this.embeddings = embeddings;
        this.numberOfCandidate = numberOfCandidate;
        this.limit = limit;
        this.minScore = minScore;
        this.metadata = metadata;
    }

    public ChatInputQuery(String sessionId, List<Double> embeddings) {
        this(sessionId, embeddings, 100, 10, 0.0, null);
    }
}
