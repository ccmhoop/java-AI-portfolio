package com.conner.assistant.controllers;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
public class OllamaController {

    private final OllamaChatModel chatModel;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    public OllamaController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    //TODO Cleanup / separate vector similarity search & templates
    @GetMapping("/ai/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "photo services") String message) {
        List<Document> results = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(3));
        String instruction = "You're a chatbot working for webdevbuilders";
        StringBuilder context = new StringBuilder();
        for (Document result : results) {
            context.append(result.getContent());
        }
        String llamaChatTemplate =
                "<|start_header_id|>system<|end_header_id|>" + instruction + "<|eot_id|> " +
                        "<|start_header_id|>context<|end_header_id|>" + context + "<|eot_id|> " +
                        "<|start_header_id|>user<|end_header_id|> " + message + "<|eot_id|> " +
                        "<|start_header_id|>assistant<|end_header_id|> Provide information <|eot_id|> " +
                        "<|start_header_id|>response<|end_header_id|>";
        return Map.of("generation", chatModel.call(llamaChatTemplate));
    }

    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatModel.stream(prompt);
    }

}