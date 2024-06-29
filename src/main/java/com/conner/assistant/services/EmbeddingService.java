package com.conner.assistant.services;

import com.conner.assistant.utils.TextNormalizer;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class EmbeddingService {

    @Autowired
    private VectorStore vectorStore;

    //TODO Error Handling, adapt to different types of files
    public void createDocumentEmbeddings() throws IOException {
        var splitText = TextNormalizer.textRemoveEmptyLines("src/main/resources/docs/test.txt");
        TokenTextSplitter textSplitter = new TokenTextSplitter();
        var document = new Document(splitText, Map.of("meta:" + 1, "meta:" + 1));
        var splitDocument = textSplitter.apply(List.of(document));
        vectorStore.add(splitDocument);
    }
}
