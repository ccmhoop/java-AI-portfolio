package com.conner.assistant.ollama;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OllamaService {

    @Autowired
    private OllamaUtility ollamaUtility;

    @Autowired
    private OllamaChatModel chatModel;

    //TODO Error Handling

    /**
     * Generates a response using the llama3 AI model based on the given prompt.
     *
     * @param prompt the input prompt string
     * @return the generated response
     */
    public String generateLlama(String prompt) {
        Prompt llama3Prompt = new Prompt(
                ollamaUtility.llamaChatTemplate(prompt),
                OllamaOptions.create()
                        .withTemperature(0.5f)
                        .withTopK(5)
                        .withTopP(1f)
                        .withModel("llama3")
        );

        String llama3Response = chatModel.call(llama3Prompt)
                .getResult()
                .getOutput()
                .getContent();

        ollamaUtility.chatHistoryBuilder(prompt, llama3Response);

        return llama3Response;
    }

}
