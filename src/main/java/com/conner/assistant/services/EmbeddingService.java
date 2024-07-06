package com.conner.assistant.services;

import com.conner.assistant.utils.TextNormalizer;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class represents a service for creating document embeddings.
 * It uses various utility classes for text normalization, text splitting, and metadata generation.
 * The embeddings are added to a vector store for later retrieval.
 */
@Service
public class EmbeddingService {

    @Autowired
    private VectorStore vectorStore;

    //TODO Error Handling, adapt to different types of files
    /**
     * Creates document embeddings by normalizing the text, splitting it, and adding it to the vector store.
     *
     * @return true if the document embeddings are successfully created and added to the vector store, false otherwise
     */
    public HttpStatus createDocumentEmbeddings() {

        Optional<String> normalizedText = TextNormalizer.removeEmptySpaces("src/main/resources/docs/test.txt");
        if(normalizedText.isEmpty()) {
            return HttpStatus.CONFLICT;
        }

        TextSplitter textSplitter = new TokenTextSplitter(256, 128, 5, 10000, true);
        Document initialDocument = new Document(normalizedText.get(), Map.of("metadata", "metadata"));

        List<Document> documentList = textSplitter.split(initialDocument).stream()
                .map(document -> new Document(document.getContent(), Map.of("metadata", generateUniqueMetadata())))
                .collect(Collectors.toList());

        vectorStore.add(documentList);
        return HttpStatus.ACCEPTED;
    }

    //TODO generate proper metadata
    private String generateUniqueMetadata() {
        return "webdevbuilders : " + UUID.randomUUID();
    }

}
