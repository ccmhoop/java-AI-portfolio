package com.conner.assistant.ollama;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class OllamaController {

    @Autowired
    private OllamaService ollamaService;

    //TODO split method into token checkers
    @GetMapping("/generateLlama3")
    public String generate(@RequestParam String prompt) {
        return ollamaService.generateLlama(prompt);
    }

//    @GetMapping("/generateStream")
//    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
//        Prompt prompt = new Prompt(new UserMessage(message));
//        return chatModel.stream(prompt);
//    }

}