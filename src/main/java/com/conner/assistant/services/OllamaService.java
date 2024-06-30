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

    //TODO Error Handling
    public Prompt generateLlama(String prompt) {
        return new Prompt(
                ollamaUtility.llamaChatTemplate(prompt),
                OllamaOptions.create()
                        .withTemperature(0.5f)
                        .withTopK(5)
                        .withTopP(1f)
                        .withModel("llama3")
        );
    }
}
