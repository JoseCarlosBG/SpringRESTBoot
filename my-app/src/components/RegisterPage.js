import React, { useState } from 'react';
import Header from './Header';
import axios from 'axios'; // Import Axios for making HTTP requests
import './styles/register.css';

const RegisterPage = () => {
    // State variables to store form data
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        repeatPassword: '',
    });

    // Handle form field changes
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    // Handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/SpringRESTBoot/api/v1/auth/signup', formData);
            console.log('Registration successful:', response.data.message);
            // Optionally, you can redirect the user to another page upon successful registration
        } catch (error) {
            console.error('Registration failed:', error.response.data.message);
            // Optionally, you can display an error message to the user
        }
    };

    return (
        <div>
            <Header />
            <main>
                <section className="section_reg">
                    <h1 className="header">Register</h1>
                    <form className="form" onSubmit={handleSubmit}>
                        <fieldset className="registerbox">
                            <div className="grid-register">
                                <div className="grid-user">
                                    Login name:
                                    <p><input type="text" name="username" value={formData.username} onChange={handleChange} /></p>
                                </div>
                                <div className="grid-email">
                                    Email:
                                    <p><input type="text" name="email" value={formData.email} onChange={handleChange} /></p>
                                </div>
                                <div className="grid-pass">
                                    Password:
                                    <p><input type="password" name="password" value={formData.password} onChange={handleChange} /></p>
                                </div>
                                <div className="grid-repeat">
                                    Repeat password:
                                    <p><input type="password" name="repeatPassword" value={formData.repeatPassword} onChange={handleChange} /></p>
                                </div>
                                <div className="grid-buttons">
                                    <button className="btn" type="button">Cancel</button>
                                    <button className="btn" type="submit">Sign Up</button>
                                </div>
                            </div>
                        </fieldset>
                    </form>
                </section>
            </main>
        </div>
    );
};

export default RegisterPage;
