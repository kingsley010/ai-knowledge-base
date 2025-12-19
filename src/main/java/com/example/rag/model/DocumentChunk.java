package com.example.rag.model;

public class DocumentChunk {
    private final String text;
    private final double[] embedding;

    public DocumentChunk(String text, double[] embedding) {
        this.text = text;
        this.embedding = embedding;
    }

    public String getText() {
        return text;
    }

    public double[] getEmbedding() {
        return embedding;
    }
}
