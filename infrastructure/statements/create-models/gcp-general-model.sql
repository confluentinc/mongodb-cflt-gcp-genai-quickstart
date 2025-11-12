CREATE MODEL GCPGeneralModel INPUT (text STRING) OUTPUT (response STRING) COMMENT 'General model with no system prompt.'
WITH
    (
        'task' = 'text_generation',
        'provider' = 'googleai',
        'googleai.connection' = 'gcp-gemini-connection',
        'googleai.client_timeout' = '120',
        'googleai.system_prompt' = ''
    );