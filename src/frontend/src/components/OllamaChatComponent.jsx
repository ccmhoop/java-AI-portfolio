import { useState } from 'react';
import axios from "axios";

export default function OllamaChatComponent() {

    const [ prompt, setPrompt] = useState('');
    const [ aiResponse, setAiResponse ] = useState( '');

    const handleChatInput = async (e) => {
        e.preventDefault();
        let config = {
            method: 'get',
            maxBodyLength: Infinity,
            url: `http://localhost:8080/ai/generateLlama3?prompt=${prompt}`,
            headers: { }
        };

        await axios.request(config)
            .then((response) => {
                setAiResponse(response.data);
                console.log(JSON.stringify(response.data));
            })
            .catch((error) => {
                console.log(error);
            });
    };

    return (<>
        <textarea readOnly={true} value={aiResponse}></textarea>
        <form onSubmit={handleChatInput}>
            <input
                type="text"
                value={prompt}
                onChange={(e) => setPrompt(e.target.value)}
                placeholder="Ask LLama3"
                required
            />
            <button type="submit">Submit</button>
        </form>
        </>
    );
}