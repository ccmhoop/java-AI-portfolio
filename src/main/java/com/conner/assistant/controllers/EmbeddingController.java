package com.conner.assistant.controllers;

import com.conner.assistant.services.EmbeddingService;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    public EmbeddingController(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    //TODO String[] Message adapt to frontend, add error handling, add http status, improve text splitter
    @PostMapping("/embedDocument")
    public HttpStatus embedDocument() {
        return embeddingService.createDocumentEmbeddings();
    }

}