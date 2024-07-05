import { useState } from 'react';
import axios from 'axios';

function LoginComponent() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            await axios.post('http://localhost:8080/auth/login', {
                username,
                password
            }, {
                headers: {
                    'Content-Type': 'application/json',
                },
                withCredentials: true
            });
        } catch (error) {
            console.error('Error logging in:', error);
        }
    };

    return (
        <form onSubmit={handleLogin}>
            <input
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="Username"
                required
            />
            <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Password"
                required
            />
            <button type="submit">Login</button>
        </form>
    );
}

export default LoginComponent;
