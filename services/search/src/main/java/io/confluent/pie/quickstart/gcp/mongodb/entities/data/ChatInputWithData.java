package io.confluent.pie.quickstart.gcp.mongodb.entities.data;

import io.confluent.kafka.schemaregistry.annotations.Schema;
import io.confluent.pie.quickstart.gcp.mongodb.entities.Metadata;
import io.confluent.pie.quickstart.gcp.mongodb.entities.Product;

import java.util.List;

@Schema(value = """
        {
          "properties": {
            "medication_summaries": {
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
                          "alcohol_interaction": {
                            "connect.index": 15,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "type": "string"
                              }
                            ]
                          },
                          "brand_names": {
                            "connect.index": 3,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "items": {
                                  "type": "string"
                                },
                                "type": "array"
                              }
                            ]
                          },
                          "contraindications": {
                            "connect.index": 16,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "items": {
                                  "type": "string"
                                },
                                "type": "array"
                              }
                            ]
                          },
                          "cost_per_unit": {
                            "connect.index": 19,
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
                          "country_availability": {
                            "connect.index": 20,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "items": {
                                  "type": "string"
                                },
                                "type": "array"
                              }
                            ]
                          },
                          "disclaimers": {
                            "connect.index": 10,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "items": {
                                  "type": "string"
                                },
                                "type": "array"
                              }
                            ]
                          },
                          "dosage_form": {
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
                          "drug_class": {
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
                          "duration_in_days": {
                            "connect.index": 8,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "connect.type": "int64",
                                "type": "number"
                              }
                            ]
                          },
                          "expiry_date": {
                            "connect.index": 21,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "type": "string"
                              }
                            ]
                          },
                          "frequency_per_day": {
                            "connect.index": 7,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "connect.type": "int64",
                                "type": "number"
                              }
                            ]
                          },
                          "generic_name": {
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
                          "id": {
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
                          "illnesses_treated": {
                            "connect.index": 11,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "items": {
                                  "type": "string"
                                },
                                "type": "array"
                              }
                            ]
                          },
                          "manufacturers": {
                            "connect.index": 18,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "items": {
                                  "type": "string"
                                },
                                "type": "array"
                              }
                            ]
                          },
                          "name": {
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
                          "pregnancy_category": {
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
                          "prescription_required": {
                            "connect.index": 13,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "type": "boolean"
                              }
                            ]
                          },
                          "route_of_administration": {
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
                          "side_effects": {
                            "connect.index": 9,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "items": {
                                  "type": "string"
                                },
                                "type": "array"
                              }
                            ]
                          },
                          "storage_instructions": {
                            "connect.index": 17,
                            "oneOf": [
                              {
                                "type": "null"
                              },
                              {
                                "type": "string"
                              }
                            ]
                          },
                          "strength": {
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
                          "summary": {
                            "connect.index": 22,
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
        }
        """, refs = {})
public record ChatInputWithData(
        String sessionId,
        List<Medication> results,
        String medication_summaries,
        Metadata metadata) {
}