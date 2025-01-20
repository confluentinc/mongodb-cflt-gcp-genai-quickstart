#!/usr/bin/env bash
# this script is used to create a connection in the confluent cloud cluster by interacting with the rest api that the confluent cli uses.
# At the time of writing this script, the terraform provider does not support creating/managing flink connections in the confluent cloud cluster.
# It should be replaced or removed once its provided by the terraform provider.

set -oe pipefail

# Required environment variables
REQUIRED_ENV_VARS=(
  "FLINK_API_KEY" "FLINK_API_SECRET" "FLINK_ENV_ID" "FLINK_ORG_ID"
  "FLINK_REST_ENDPOINT" "GCP_SERVICE_ACCOUNT_KEY_FILE" "GCP_GEMINI_API_KEY" "GCP_REGION" "GCP_PROJECT_ID"
)

# Check if required environment variables are set
for env_var in "${REQUIRED_ENV_VARS[@]}"; do
  if [ -z "${!env_var}" ]; then
    echo "Error: $env_var is not set"
    exit 1
  fi
done

# Encode API key and secret for basic authentication
BASIC_AUTH=$(echo -n "$FLINK_API_KEY:$FLINK_API_SECRET" | base64 -w 0)

CONTENT=$(cat "$GCP_SERVICE_ACCOUNT_KEY_FILE")

# Prepare GCP authentication data
SERVICE_KEY=$(jq -n -r --arg service_key "$CONTENT" '{SERVICE_KEY: $service_key}' | jq '.|tostring')
API_KEY=$(jq -n -r --arg api_key "$GCP_GEMINI_API_KEY" '{API_KEY: $api_key}' | jq '.|tostring')

# Create connection in Confluent Cloud cluster
echo
curl --request POST \
  --url "$FLINK_REST_ENDPOINT/sql/v1/organizations/$FLINK_ORG_ID/environments/$FLINK_ENV_ID/connections" \
  --header "Authorization: Basic $BASIC_AUTH" \
  --header "content-type: application/json" \
  --data '{
    "name": "gcp-embed-connection",
    "spec": {
      "connection_type": "VERTEXAI",
      "endpoint": "https://'"$GCP_REGION"'-aiplatform.googleapis.com/v1/projects/'"$GCP_PROJECT_ID"'/locations/'"$GCP_REGION"'/publishers/google/models/text-embedding-004:predict",
      "auth_data": {
        "kind": "PlaintextProvider",
        "data": '"$SERVICE_KEY"'
      }
    }
  }' | jq . > gcp-embed-connection-result.json

 # Create connection in Confluent Cloud cluster
 curl --request POST \
   --url "$FLINK_REST_ENDPOINT/sql/v1/organizations/$FLINK_ORG_ID/environments/$FLINK_ENV_ID/connections" \
   --header "Authorization: Basic $BASIC_AUTH" \
   --header "content-type: application/json" \
   --data '{
     "name": "gcp-gemini-connection",
     "spec": {
       "connection_type": "GOOGLEAI",
       "endpoint": "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent",
       "auth_data": {
         "kind": "PlaintextProvider",
         "data": '"$API_KEY"'
       }
     }
   }' | jq . > gcp-gemini-connection-result.json