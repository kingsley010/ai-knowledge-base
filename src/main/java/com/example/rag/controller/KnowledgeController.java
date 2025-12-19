package com.example.rag.controller;

import com.example.rag.service.DocumentService;
import com.example.rag.service.RagService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {
    private final DocumentService documentService;
    private final RagService ragService;

    public KnowledgeController(DocumentService documentService, RagService ragService) {
        this.documentService = documentService;
        this.ragService = ragService;
    }

    @PostMapping("/upload")
    public String upload(@RequestBody String text) {
        documentService.ingest(text);
        return "Document ingested";
    }

    @PostMapping("/ask")
    public Map<String, String> ask(@RequestBody Map<String, String> body) {
        String answer = ragService.ask(body.get("question"));
        Map<String, String> response = new HashMap<>();
        response.put("answer", answer);
        return response;
    }
}
