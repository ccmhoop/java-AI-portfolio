import {useState} from 'react';
import {axiosPostLogin} from "../helpers/axiosPresets.js";

export default function LoginComponent() {
    const [loginFormData, setLoginFormData] = useState({
        username: "",
        password: ""
    });

    const handleChange = (e) => {
        setLoginFormData({
            ...loginFormData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axiosPostLogin({
                "username": loginFormData.username,
                "password": loginFormData.password
            });
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <input
                type="text"
                name="username"
                value={loginFormData.username}
                onChange={handleChange}
                placeholder="Username"
                required
            />
            <input
                type="password"
                name="password"
                value={loginFormData.password}
                onChange={handleChange}
                placeholder="Password"
                required
            />
            <button type="submit">Login</button>
        </form>
    );
}

