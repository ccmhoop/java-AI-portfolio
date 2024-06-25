package com.conner.assistant.controllers;

import com.conner.assistant.utils.FileReader;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class EmbeddingController {

    @Autowired
    private VectorStore vectorStore;

    //TODO String[] Message adapt to frontend,add error handling, add http status
    @GetMapping("/ai/embedDocument")
    public void embedDocument(@RequestParam(value = "message", defaultValue = "Tell me a joke") String[] message) throws IOException {
        message = FileReader.textFileReader("src/main/resources/docs/test.txt");
        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < message.length; i++) {
            documents.add(new Document(message[i], Map.of("meta:" + i, "meta:" + i)));
        }
        vectorStore.add(documents);
        //TODO use syntax for OllamaChat/Rag ->
        List<Document> results = vectorStore.similaritySearch(SearchRequest.query("what is in the gold package").withTopK(1));
        System.out.println(results);
    }

}