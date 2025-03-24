CREATE MODEL GCPVertexAIEmbed INPUT (text STRING) OUTPUT (embeddings ARRAY < FLOAT >)
WITH
    (
        'vertexai.connection' = 'gcp-embed-connection',
        'task' = 'embedding',
        'provider' = 'vertexai'
    );