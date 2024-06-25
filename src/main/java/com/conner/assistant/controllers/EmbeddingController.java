package com.conner.assistant.controllers;



import com.conner.assistant.utils.FileReader;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class EmbeddingController {

    private final EmbeddingModel embeddingModel;

    @Autowired
    public EmbeddingController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @GetMapping("/ai/embedding")
    public Map embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) throws IOException {
        message = FileReader.textFileReader("src/main/resources/docs/test.txt");
        EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
        return Map.of("embedding", embeddingResponse);
    }
}