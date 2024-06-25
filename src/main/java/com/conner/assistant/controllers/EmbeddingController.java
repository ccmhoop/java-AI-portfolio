package com.conner.assistant.controllers;

import com.conner.assistant.utils.TextSplitter;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    //TODO String[] Message adapt to frontend, add error handling, add http status
    @PostMapping("/ai/embedDocument")
    public void embedDocument() throws IOException {
        List<String> message = TextSplitter.splitText("src/main/resources/docs/test.txt");
        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < message.size(); i++) {
            documents.add(new Document(message.get(i), Map.of("meta:"+i, "meta:"+i)));
        }
        vectorStore.add(documents);
        //TODO Tex splitter needs to be optimized use syntax for OllamaChat/Rag ->
        List<Document> results = vectorStore.similaritySearch(SearchRequest.query("i want a video for my website").withTopK(10));
        //Test vector similarity results
        for (int i = 0; i < results.size(); i++) {
            System.out.println(results.get(i).getContent());
        }
    }

}