package com.conner.assistant.services;

import com.conner.assistant.utils.OllamaUtility;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OllamaService {

    @Autowired
    private OllamaUtility ollamaUtility;

    public Prompt generateLlama(String prompt) {
        return new Prompt(
                ollamaUtility.llamaChatTemplate(prompt),
                OllamaOptions.create()
                        .withModel("llama3")
                        .withTemperature(0.7f)
                        .withTopK(5)
        );
    }
}
