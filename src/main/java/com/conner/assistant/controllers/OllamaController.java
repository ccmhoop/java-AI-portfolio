package com.conner.assistant.controllers;

import com.conner.assistant.dto.LoginResponseDTO;
import com.conner.assistant.services.AuthenticationService;
import com.conner.assistant.services.OllamaService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@RestController
@RequestMapping("/ai")
public class OllamaController {

    private final OllamaChatModel chatModel;

    @Autowired
    private OllamaService ollamaService;

    private final AuthenticationService authenticationService;


    @Autowired
    public OllamaController(OllamaChatModel chatModel, AuthenticationService authenticationService) {
        this.chatModel = chatModel;
        this.authenticationService = authenticationService;
    }

    //TODO error handling
    @GetMapping("/generateLlama3")
    public String generate(@RequestParam String prompt, HttpServletRequest request, HttpServletResponse response) {
        //testing JWt
        Cookie[] cookies = request.getCookies();

        System.out.println(cookies[0]);

        return chatModel.call(ollamaService.generateLlama(prompt)).getResult().getOutput().getContent();
    }

    @GetMapping("/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatModel.stream(prompt);
    }

}