# Frequently Asked Questions

## Terraform

### Why does my terraform apply take so long?

Certain resource types like aws cloudfront, sink connectors can take a long time to fully apply (~5mins). On average, a
terraform apply. In addition, we are also building all the lambda functions and packaging them as part of the apply. The
entire thing should take no should take around ~15 minutes to complete.

## Flink

### Received bad response code 403 message

If you see the following error message either via email or the confluent UI, it is likely due to the AWS credentials
expiring. This is common if you are providing an aws session token.

```json
{
  "message": "The security token included in the request is expired"
}
```

Consider refreshing the credentials, using longer lived credentials and destroying the stack and reapplying it.

Alternatively, you can use the confluent cli to update your flink connections.
See https://docs.confluent.io/confluent-cli/current/command-reference/flink/connection/confluent_flink_connection_update.html

```bash
confluent flink connection update bedrock-titan-embed-connection \
--cloud AWS \
--region us-east-1 \
--aws-access-key $AWS_ACCESS_KEY_ID \
--aws-secret-key $AWS_SECRET_ACCESS_KEY \
--aws-session-token $AWS_SESSION_TOKEN

confluent flink connection update bedrock-claude-3-haiku-connection \
--cloud AWS \
--region us-east-1 \
--aws-access-key $AWS_ACCESS_KEY_ID \
--aws-secret-key $AWS_SECRET_ACCESS_KEY \
--aws-session-token $AWS_SESSION_TOKEN
```

Once you have updated the connections, you can restart the failing flink statement

## Chatbot

### Why is my first message to the chatbot slow?

This quickstart is entirely serverless and uses a lambda functions to handle the chatbot requests. The first request to
the chatbot is slow because the lambda function is cold and needs to be warmed up. Subsequent requests will be faster.

### After logging in, my chatbox is greyed out

If you see the chatbox greyed out after logging in, it is likely due to the websocket connection being closed. This can
happen after a long period of inactivity. To resolve this, simply refresh the page and log back in.

confluent flink connection create gemini-connection \
--cloud GCP \
--region us-central1 \
--endpoint https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent
--api-key AIzaSyAJe59B1IaLlr5jYw1Gv99RU1a-swX8kL4
--type googleai \
--environment env-d1621d \

confluent flink connection create googleai-cli-connection \
--cloud GCP \
--region us-east4 \
--endpoint https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent \
--api-key $API_KEY \
--type googleai \
--environment env-oqv0dy