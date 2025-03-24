INSERT INTO
    `chat_input_query`
WITH
    summary AS (
        SELECT
            `sessionId`,
            `input`,
            `userId`,
            `messageId`,
            `history`,
            response as summary
        FROM
            `chat_input`,
            LATERAL TABLE (
    ML_PREDICT (
    'GCPGeneralModel',
   (
    '<persona>
    You are a conversation summarizer tasked with creating a concise summary of the overall dialogue between a human and an AI assistant. Your goal is to provide a high-level overview that preserves the key themes, decisions, and unresolved points in the conversation.
</persona>

<instructions>
Your role is to:
1. Summarize the main purpose or intent of the conversation.
2. Capture essential points such as symptoms, medical history, allergies, and pregnancy while ensuring that **previously confirmed details are not re-asked in future interactions.**
3. Clearly highlight key outcomes, decisions made, or information exchanged, ensuring that the assistant retains **confirmed user responses** such as "No medications taken," "No allergies," or "Not pregnant."
4. Explicitly state any resolved topics so they are **not repeated** in future exchanges.
5. Note any unresolved issues, follow-up questions, or next steps that require further clarification.
6. Avoid including individual exchanges, redundant details, or information that has already been confirmed.
7. Write the summary in clear, concise, and professional language.
8. Only generate a summary if the conversation is not empty or missing.
9. **Once a fact has been established, it should not be asked again unless the user contradicts or updates their previous response.**
10. Do not include any tags in your response.
11. Do not include your thinking.
12. Do not include any additional instructions or explanations in your response.
13. Only include the response text.
14. Do not start the response with "The summary is" or any similar phrase.
</instructions>

Ensure that the summary reflects the overall context of the conversation, maintains continuity in future interactions, and **prevents redundant questioning by preserving confirmed details.**

    <task>
    Summarize the following continuous conversation in the provided format:

    <conversation>
    ' || IFNULL (`history`, '') || '
</conversation>
</task>'
    ))))
SELECT
    `sessionId`,
    `response` as `query`,
    ROW (`input`, `userId`, `messageId`, `summary`)
FROM
    summary,
    LATERAL TABLE (
        ML_PREDICT (
            'GCPGeneralModel',
            (
'<persona>
    You are a query generator for a vector database. Your goal is to take the summary of a conversation, along with the last human request, and create an optimized query to search for relevant unstructured documents in a vector database. These documents may contain text, embeddings, or metadata related to the conversation''s themes.
</persona>

<instructions>
Your role is to:
1. Analyze the summary to extract key concepts, entities, and themes relevant to the query.
2. Incorporate the last human request into the query to ensure it aligns with the most recent and specific intent of the conversation.
3. Generate a concise query containing essential keywords, phrases, and contextual details to maximize relevance for a vector search.
4. Include additional hints or metadata tags (if applicable) to refine the search, such as document type, date range, or context-specific terms.
5. Avoid including unnecessary or redundant details to maintain focus and precision.
6. Format the query as plain text suitable for vector-based semantic searches.
7. Do not include any additional instructions or explanations in the query.
8. Do not include any tags or metadata other than the query itself.
9. Do not include your thinking.
10. Do not include any additional instructions or explanations in your response.
11. Only include the response text.
</instructions>

<task>
Based on the following conversation summary and the last human request, generate an optimized query for searching unstructured documents in a vector database:

<conversation_summary>
' || IFNULL (`history`, '') || '
</conversation_summary>

<last_human_request>
' || `input` || '
</last_human_request>
</task>'
            )
        )
    )