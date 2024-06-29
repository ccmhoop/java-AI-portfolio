package com.conner.assistant.services;

import com.conner.assistant.utils.TextNormalizer;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmbeddingService {

    @Autowired
    private VectorStore vectorStore;

    //TODO Error Handling, adapt to different types of files
    public void createDocumentEmbeddings() throws IOException {
        String normalizedText = TextNormalizer.removeEmptySpaces("src/main/resources/docs/test.txt");
        TextSplitter textSplitter = new TokenTextSplitter();
        Document initialDocument = new Document(normalizedText, Map.of("meta:", "meta:"));

        List<Document> documentList = textSplitter.apply(List.of(initialDocument)).stream()
                .map(chunk -> new Document(chunk.getContent(), Map.of("meta", generateUniqueMetadata())))
                .collect(Collectors.toList());

        vectorStore.add(documentList);
    }

    private String generateUniqueMetadata() {
        return "webdevbuilders : " + UUID.randomUUID();
    }

}
