package io.confluent.pie.quickstart.gcp.mongodb.entities;

import io.confluent.kafka.schemaregistry.annotations.Schema;

import java.util.List;

@Schema(value = """
        {
          "properties": {
            "metadata": {
              "connect.index": 3,
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
            "product_summaries": {
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
            "requestId": {
              "connect.index": 0,
              "type": "string"
            },
            "results": {
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
                        "properties": {
                          "createdAt": {
                            "connect.index": 12,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "type": "string"
                              }
                            ]
                          },
                          "currency": {
                            "connect.index": 5,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "type": "string"
                              }
                            ]
                          },
                          "description": {
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
                          "name": {
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
                          "product_id": {
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
                          "rate_table": {
                            "connect.index": 11,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "type": "string"
                              }
                            ]
                          },
                          "ref_link": {
                            "connect.index": 14,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "type": "string"
                              }
                            ]
                          },
                          "repayment_frequency": {
                            "connect.index": 8,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "type": "string"
                              }
                            ]
                          },
                          "risk_level": {
                            "connect.index": 9,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "type": "string"
                              }
                            ]
                          },
                          "status": {
                            "connect.index": 10,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "type": "string"
                              }
                            ]
                          },
                          "summary": {
                            "connect.index": 1,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "type": "string"
                              }
                            ]
                          },
                          "term_max_length": {
                            "connect.index": 7,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "type": "string"
                              }
                            ]
                          },
                          "term_min_length": {
                            "connect.index": 6,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "type": "string"
                              }
                            ]
                          },
                          "type": {
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
                          "updatedAt": {
                            "connect.index": 13,
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
                        "title": "Record_results",
                        "type": "object"
                      }
                    ]
                  },
                  "type": "array"
                }
              ]
            }
          },
          "required": [
            "requestId"
          ],
          "title": "Record",
          "type": "object"
        }""", refs = {})
public record ChatInputWithData(
        String requestId,
        List<Result> results,
        String productSummaries,
        Metadata metadata) {

    public record Result(
            String productId,
            String name,
            String description,
            String currency,
            String termMinLength,
            String termMaxLength,
            String repaymentFrequency,
            String riskLevel,
            String status,
            String rateTable,
            String createdAt,
            String updatedAt,
            String refLink,
            String summary) {

        public static Result fromProduct(Product product) {
            return new Result(
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getCurrency(),
                    product.getTermMinLength(),
                    product.getTermMaxLength(),
                    product.getRepaymentFrequency(),
                    product.getRiskLevel(),
                    product.getStatus(),
                    product.getRateTable(),
                    product.getCreatedAt(),
                    product.getUpdatedAt(),
                    product.getRefLink(),
                    product.getSummary()
            );
        }
    }
}