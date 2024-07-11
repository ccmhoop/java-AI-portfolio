package com.conner.assistant.utils;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OllamaUtility {

    @Autowired
    private VectorStore vectorStore;

    private final static String SYSTEM_INSTRUCTION = "You're an agent working for webdevbuilders.";

    /**
     * Generates a chat template for the llama3 AI model.
     *
     * @param prompt the input prompt string
     * @return a chat template for the llama assistant
     */
    public String llamaChatTemplate(String prompt) {
        return "<|start_header_id|>system<|end_header_id|>" + SYSTEM_INSTRUCTION + "<|eot_id|> " +
                "<|start_header_id|>context<|end_header_id|>" + generateContext(prompt) + "<|eot_id|> " +
                "<|start_header_id|>user<|end_header_id|> " + prompt + "<|eot_id|> " +
                "<|start_header_id|>assistant<|end_header_id|> Provide information <|eot_id|> " +
                "<|start_header_id|>response<|end_header_id|>";
    }

    //Adapt code when needed or split method
    private String generateContext(String prompt) {
        StringBuilder contextStringBuilder = new StringBuilder();

        List<Document> documents = vectorStore.similaritySearch(SearchRequest.defaults()
                .withTopK(4)
                .withSimilarityThreshold(0.3)
                .withQuery(prompt)
        );

        for (int i = 0; i < documents.size(); i++) {
            contextStringBuilder.append("Retrieved document : ")
                    .append(i)
                    .append(" { ")
                    .append(documents.get(i).getContent())
                    .append(" }\n");
        }

        return contextStringBuilder.toString();
    }

}