import axios from "axios";

const config = {
    login: {
        headers: {
            'Content-Type': 'application/json',
        },
        withCredentials: true
    },
    ollama: {
        maxBodyLength: Infinity,
        withCredentials: true,
    },
}

const axiosPostTestRag = async () => await axios
    .post('http://localhost:8080/rag/embedDocument');

const axiosPostLogin = async (loginFormData) => await axios
    .post('http://localhost:8080/auth/login', loginFormData, config.login);

const axiosGetOllama = async (prompt) => await axios
    .get(`http://localhost:8080/ai/generateLlama3?prompt=${prompt}`, config.ollama);

export {axiosGetOllama, axiosPostLogin, axiosPostTestRag}
