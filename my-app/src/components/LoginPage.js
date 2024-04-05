import React, { useState } from 'react';
import axios from 'axios';
import { useHistory } from 'react-router-dom';
import './styles/login.css';

const LoginPage = ({ onLogin }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errors, setErrors] = useState({});
    const history = useHistory();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const newErrors = {};

        if (!username) {
            newErrors.username = 'Username is required.';
        } else if (username.length < 3 || username.length > 30) {
            newErrors.username = 'Username must be between 3 and 30 characters.';
        }

        if (!password) {
            newErrors.password = 'Password is required.';
        } else if (password.length < 4 || password.length > 30) {
            newErrors.password = 'Password must be between 4 and 30 characters.';
        }

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            return;
        }

        try {
            const response = await axios.post('http://localhost:8080/SpringRESTBoot/api/v1/auth/login', {
                username,
                password
            });
            const authToken = response.data.token;
            onLogin(authToken);
            console.log('Login successful');
            history.push('/certificates');
        } catch (error) {
            console.error('Login failed:', error);
            if (error.response && error.response.status === 401) {
                setErrors({ server: 'Login or password is incorrect.' });
            } else {
                setErrors({ server: 'An unexpected error occurred. Please try again.' });
            }
        }
    };

    return (
        <main>
            <div className="login-box">
                <div className="logo-circle">Login</div>
                <form onSubmit={handleSubmit}>
                    <div className="input-wrapper">
                        <input
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            placeholder="Login"
                        />
                        {errors.username && <div className="error-message">{errors.username}</div>}
                    </div>
                    <div className="input-wrapper">
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="Password"
                        />
                        {errors.password && <div className="error-message">{errors.password}</div>}
                    </div>
                    {errors.server && <div className="error-message">{errors.server}</div>}
                    <button type="submit" className="login">Login</button>
                </form>
            </div>
        </main>
    );
};

export default LoginPage;
