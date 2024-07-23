package com.conner.assistant.ollama;

import com.conner.assistant.security.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class OllamaController {


    @Autowired
    private OllamaService ollamaService;
    @Autowired
    private AuthenticationService authenticationService;


    //TODO split method into token checkers
    @GetMapping("/generateLlama3")
    public String generate(@RequestParam String prompt, HttpServletRequest request) {
        if (!authenticationService.verifyTokens(request)) {
            return "Invalid Token";
        }
        return ollamaService.generateLlama(prompt);
    }

//    @GetMapping("/generateStream")
//    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
//        Prompt prompt = new Prompt(new UserMessage(message));
//        return chatModel.stream(prompt);
//    }

}