package com.example.rag.service;

import com.example.rag.model.DocumentChunk;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {
    private final EmbeddingService embeddingService;
    private final VectorStore vectorStore;

    public DocumentService(EmbeddingService embeddingService, VectorStore vectorStore) {
        this.embeddingService = embeddingService;
        this.vectorStore = vectorStore;
    }

    public void ingest(String text) {
        for (String chunk : chunk(text)) {
            double[] embedding = embeddingService.embed(chunk);
            vectorStore.add(new DocumentChunk(chunk, embedding));
        }
    }

    private List<String> chunk(String text) {
        int size = 500;
        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < text.length(); i += size) {
            chunks.add(text.substring(i, Math.min(text.length(), i + size)));
        }
        return chunks;
    }
}
