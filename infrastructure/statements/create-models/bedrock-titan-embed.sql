CREATE MODEL BedrockTitanEmbed INPUT (text STRING) OUTPUT (embeddings ARRAY < FLOAT >)
WITH
    (
        'bedrock.connection' = 'gcp-embed-connection',
        'task' = 'embedding',
        'provider' = 'bedrock'
    );