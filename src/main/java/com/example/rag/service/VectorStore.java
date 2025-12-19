package com.example.rag.service;

import com.example.rag.model.DocumentChunk;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VectorStore {
    private final List<DocumentChunk> store = new ArrayList<>();

    public void add(DocumentChunk chunk) {
        store.add(chunk);
    }

    public List<DocumentChunk> search(double[] queryVector, int topK) {
        return store.stream()
                .sorted(Comparator.comparingDouble(c -> -cosineSimilarity(queryVector, c.getEmbedding())))
                .limit(topK)
                .collect(Collectors.toList());
    }

    private double cosineSimilarity(double[] a, double[] b) {
        double dot = 0, magA = 0, magB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            magA += a[i] * a[i];
            magB += b[i] * b[i];
        }
        return dot / (Math.sqrt(magA) * Math.sqrt(magB));
    }
}
