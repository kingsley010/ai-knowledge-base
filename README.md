# AI Knowledge Base

A Java application for ingesting documents and answering questions using RAG (Retrieval-Augmented Generation) with OPEN API.

## Setup

1. Install dependencies: `mvn install`

2. Set up environment variables: Create a `.env` file with your Gemini/OPEN API key:

   ```
   GEMINI_API_KEY=your_gemini_key_here
   OPEN_API_KEY=your_openai_key_here
   ```

3. Run the server: `mvn spring-boot:run`

## Debugging

To debug the application:
- Run `mvn spring-boot:run` to start the server.
- Open Chrome and navigate to `chrome://inspect` to attach the debugger.
- Alternatively, use IntelliJ idea's built-in debugger by creating a launch configuration for Java.

## API Endpoints

- **POST /api/knowledge/upload**: Upload a document (send text in the request body)
- **POST /api/knowledge/ask**: Ask a question (send JSON with a "question" field, e.g., `{"question": "What is AI?"}`)

## Usage Example

After starting the server, you can use tools like curl or Postman to interact with the API.

Example curl commands:

```bash
# Upload a document
curl -X POST http://localhost:8080/api/knowledge/upload \
  -H "Content-Type: text/plain" \
  -d "Your document text here."

# Ask a question
curl -X POST http://localhost:8080/api/knowledge/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "What is the main topic?"}'
```

## Troubleshooting

- Ensure you have a valid Gemini/OPEN API key or whichever LLM you will be making use of.
- The vector store is in-memory, so data will be lost on restart.
- For production, consider using a persistent vector database.
