package com.conner.assistant.utils;

import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OllamaUtils {

    @Autowired
    private VectorStore vectorStore;

    private final static String SYSTEM_INSTRUCTION = "You're a chatbot working for webdevbuilders";

    public String llamaChatTemplate(String prompt) {
        return "<|start_header_id|>system<|end_header_id|>" + SYSTEM_INSTRUCTION + "<|eot_id|> " +
                "<|start_header_id|>context<|end_header_id|>" + generateContext(prompt) + "<|eot_id|> " +
                "<|start_header_id|>user<|end_header_id|> " + prompt + "<|eot_id|> " +
                "<|start_header_id|>assistant<|end_header_id|> Provide information <|eot_id|> " +
                "<|start_header_id|>response<|end_header_id|>";
    }

    //Adapt code when needed
    private String generateContext(String prompt) {
       return vectorStore.similaritySearch(SearchRequest.query(prompt).withTopK(3)).getFirst().getContent();
    }

}
