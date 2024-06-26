package com.conner.assistant.controllers;

import com.conner.assistant.services.EmbeddingService;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class EmbeddingController {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private EmbeddingService EmbeddingService;

    //TODO String[] Message adapt to frontend, add error handling, add http status, improve text splitter
    @PostMapping("/ai/embedDocument")
    public HttpStatus embedDocument() {
        try {
            vectorStore.add(EmbeddingService.createDocumentEmbeddings());
        } catch (IOException e) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.ACCEPTED;
    }

}