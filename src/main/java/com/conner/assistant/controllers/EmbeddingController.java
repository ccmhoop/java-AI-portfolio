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
        List<String> splitText = TextSplitter.splitText("src/main/resources/docs/test.txt");
        List<Document> documents = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < splitText.size(); i++) {
            builder.append(splitText.get(i));
//            documents.add(new Document(message.get(i), Map.of("meta:"+i, "meta:"+i)));
        }
        //placeholder until Text Splitter is finished
        documents.add(new Document(builder.toString(), Map.of("meta:"+1, "meta:"+1)));
        vectorStore.add(documents);

    }

}