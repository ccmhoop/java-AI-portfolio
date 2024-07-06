package com.conner.assistant.controllers;

import com.conner.assistant.models.RefreshToken;
import com.conner.assistant.repository.RefreshTokenRepository;
import com.conner.assistant.repository.UserRepository;
import com.conner.assistant.services.RefreshTokenService;
import com.conner.assistant.services.RegistrationService;
import com.conner.assistant.services.OllamaService;
import com.conner.assistant.services.TokenService;
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

@RestController
@RequestMapping("/ai")
public class OllamaController {

    @Autowired
    private OllamaChatModel chatModel;
    @Autowired
    private OllamaService ollamaService;
    @Autowired
    private TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    //TODO error handling set response entity change axios in front end to retrieve refresh token
    @GetMapping("/generateLlama3")
    public String generate(@RequestParam String prompt, HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            RefreshToken refreshToken = refreshTokenRepository.findByToken(cookies[1].getValue()).orElseThrow();
            refreshTokenService.verifyExpiration(refreshToken);
            return chatModel
                    .call(ollamaService.generateLlama(prompt))
                    .getResult()
                    .getOutput()
                    .getContent();
        }catch (Exception e) {
            return "No AI subscription found";
        }
    }

    @GetMapping("/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatModel.stream(prompt);
    }

}