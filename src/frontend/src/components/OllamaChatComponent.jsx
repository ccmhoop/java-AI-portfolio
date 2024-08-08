import { useState } from 'react';
import { axiosGetOllama } from '../helpers/axiosPresets.js';

export default function OllamaChatComponent() {

    const [ollamaResponse, setOllamaResponse] = useState("");
    const [prompt, setPrompt] = useState("");

    const handlePromptChange = (e) => {
        setPrompt(e.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axiosGetOllama(prompt);
            setOllamaResponse(response.data);
            setPrompt("");
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <>
            <textarea readOnly={true} value={ollamaResponse} />
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    value={prompt}
                    name="prompt"
                    onChange={handlePromptChange}
                    placeholder="Ask LLama3"
                    required
                />
                <button type="submit">Submit</button>
            </form>
        </>
    );
}