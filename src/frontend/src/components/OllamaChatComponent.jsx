import {useState} from 'react';
import {axiosGetOllama} from '../helpers/axiosPresets.js';

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
            <textarea className="llm-response" readOnly={true} value={ollamaResponse}/>
            <form id="llm-input-bar" onSubmit={handleSubmit}>
                <input
                    className="llm-input"
                    type="text"
                    value={prompt}
                    name="prompt"
                    onChange={handlePromptChange}
                    placeholder="Ask LLama3"
                    required
                />
            </form>
            <button form="llm-input-bar" className="llm-submit-button" type="submit">Submit</button>
        </>
    );
}