package com.conner.assistant.ollama;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OllamaController {

    private final ChatClient chatClient;

    public OllamaController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("You're an helpful agent answering questions about cities around the world")
                .defaultFunctions("currentWeatherFunction")
                .build();
    }

    @GetMapping("/ai/generateLlama3")
    public String generate(@RequestParam String prompt) {
        System.out.println("yo");
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
//        return ollamaService.generateLlama(prompt);
    }

//    @GetMapping("/generateStream")
//    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
//        Prompt prompt = new Prompt(new UserMessage(message));
//        return chatModel.stream(prompt);
//    }

}