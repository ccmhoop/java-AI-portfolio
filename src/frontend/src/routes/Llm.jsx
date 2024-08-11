import OllamaChatComponent from "../components/OllamaChatComponent.jsx";

export  default function Llm(){
    return (
        <div className="llm-container">
            <h1 className="llm-header">Llama 3</h1>
            <OllamaChatComponent/>
        </div>
    );
}