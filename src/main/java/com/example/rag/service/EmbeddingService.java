package com.example.rag.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmbeddingService {

    private final WebClient webClient;

    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("OPENAI_API_KEY");

    public EmbeddingService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public double[] embed(String text) {
        // Java 8 compatible map
        Map<String, Object> body = new HashMap<>();
        body.put("model", "text-embedding-3-small");
        body.put("input", text);

        // Send POST request
        Map response = webClient.post()
                .uri("/embeddings")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        // Extract embedding list
        List embedding = (List) ((Map)((List)response.get("data")).get(0)).get("embedding");

        // Convert to double array
        double[] vector = new double[embedding.size()];
        for (int i = 0; i < embedding.size(); i++) {
            vector[i] = ((Number) embedding.get(i)).doubleValue();
        }

        return vector;
    }
}
