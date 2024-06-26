package com.conner.assistant.controllers;

import com.conner.assistant.services.OllamaService;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@CrossOrigin("*")
public class OllamaController {

    private final OllamaChatModel chatModel;

    @Autowired
    private OllamaService ollamaService;

    @Autowired
    public OllamaController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    //TODO error handling
    @GetMapping("/generateLlama3")
    public String generate(@RequestParam(value = "message", defaultValue = "photo services") String prompt) {
        return chatModel.call(ollamaService.generateLlama(prompt)).getResult().getOutput().getContent();
    }

    @GetMapping("/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatModel.stream(prompt);
    }

}