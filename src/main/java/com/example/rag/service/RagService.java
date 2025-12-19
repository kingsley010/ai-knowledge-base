package com.example.rag.service;

import com.example.rag.model.DocumentChunk;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RagService {

    private final EmbeddingService embeddingService;
    private final VectorStore vectorStore;
    private final WebClient webClient;

    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("OPENAI_API_KEY");

    public RagService(EmbeddingService embeddingService, VectorStore vectorStore) {
        this.embeddingService = embeddingService;
        this.vectorStore = vectorStore;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public String ask(String question) {
        double[] qEmbedding = embeddingService.embed(question);
        List<DocumentChunk> context = vectorStore.search(qEmbedding, 5);

        // Join context strings manually (Java 8 compatible)
        StringBuilder joinedContextBuilder = new StringBuilder();
        for (int i = 0; i < context.size(); i++) {
            joinedContextBuilder.append(context.get(i).getText());
            if (i < context.size() - 1) {
                joinedContextBuilder.append("\n---\n");
            }
        }
        String joinedContext = joinedContextBuilder.toString();

        // Build prompt string
        String prompt = "Answer ONLY using the context below.\n"
                + "If the answer is not present, say \"I don’t know\".\n\n"
                + "Context:\n" + joinedContext + "\n\n"
                + "Question:\n" + question;

        // Build body map (Java 8 compatible)
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(message);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4.1-mini");
        body.put("messages", messages);

        // Send POST request
        Map response = webClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        // Extract the answer safely
        List choices = (List) response.get("choices");
        Map firstChoice = (Map) choices.get(0);
        Map messageMap = (Map) firstChoice.get("message");
        return (String) messageMap.get("content");
    }
}
