package com.conner.assistant.services;

import com.conner.assistant.utils.TextSplitter;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmbeddingService {

    //TODO Make util methods //placeholder until Text Splitter is finished
    public List<Document> createDocumentEmbeddings() throws IOException {
        List<String> splitText = TextSplitter.splitText("src/main/resources/docs/test.txt");
        List<Document> documents = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < splitText.size(); i++) {
            if (splitText.get(i) == null | splitText.get(i).isEmpty()) {
                throw new IOException();
            }
            builder.append(splitText.get(i));
//                documents.add(new Document(splitText.get(i), Map.of("meta:" + i, "meta:" + i)));
        }
        //placeholder until Text Splitter is finished
        documents.add(new Document(builder.toString(), Map.of("meta:" + 1, "meta:" + 1)));
        return documents;
    }
}
