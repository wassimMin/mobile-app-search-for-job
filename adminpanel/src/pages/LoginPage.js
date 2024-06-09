import React, { useState } from "react";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const LoginPage = ()=>{
    const navigate = useNavigate();
    const [formData,setFormData] = useState({
        email: '',
        password: ''
    });
    const [error, setError] = useState('');

    const handleChange = (e)=>{
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };
    
    const handleSubmit = (e) => {
        e.preventDefault();
        axios.post('http://localhost:5000/api/login', formData)
            .then(response => {
                console.log(response.data);
                navigate('/ControlePanel');
              })
            .catch(error => {
                console.error('Error logging in:', error);
                setError('Invalid email or password');
            });
    };

    return (
        <section className="bg-gray-800 dark:bg-gray-900">
    <div className="flex flex-col items-center justify-center px-6 py-8 mx-auto md:h-screen lg:py-1">
        <a href="#" className="flex items-center mb-6 text-2xl font-semibold text-white dark:text-white">
            <img className="w-8 h-8 mr-2" src="/logo1.svg" alt="logo" />
            Admin Panel
        </a>
        <div className="w-full bg-gray-700 rounded-lg shadow dark:border-gray-700 md:mt-0 sm:max-w-md xl:p-0">
            <div className="p-6 space-y-4 md:space-y-6 sm:p-8">
                <h1 className="text-xl font-bold leading-tight tracking-tight text-white md:text-2xl dark:text-white">
                    Log in
                </h1>
                <form className="space-y-4 md:space-y-6" onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="email" className="block mb-2 text-sm font-medium text-white dark:text-white">Email</label>
                        <input 
                            type="email" 
                            name="email" 
                            id="email" 
                            placeholder="name@company.com" 
                            value={formData.email} 
                            onChange={handleChange} 
                            className="bg-gray-600 border border-gray-500 text-white sm:text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" 
                            required 
                        />
                    </div>
                    <div>
                        <label htmlFor="password" className="block mb-2 text-sm font-medium text-white dark:text-white">Password</label>
                        <input 
                            type="password" 
                            name="password" 
                            id="password" 
                            placeholder="••••••••" 
                            value={formData.password} 
                            onChange={handleChange} 
                            className="bg-gray-600 border border-gray-500 text-white sm:text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" 
                            required 
                        />
                    </div>
                    {error && (
                        <div role="alert" className="alert alert-error text-red-600 dark:text-red-400">
                            <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                            <span>{error}</span>
                        </div>
                    )}
                    <button type="submit" className="w-full text-white bg-primary-600 hover:bg-primary-700 focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primary-600 dark:hover:bg-primary-700 dark:focus:ring-primary-800"
                        style={{
                            transition: 'background-color 0.3s ease',
                            cursor: 'pointer', 
                            animation: 'pulse 1s infinite', 
                        }}>Log in</button>
                </form>
            </div>
        </div>
    </div>
</section>

    );
};

export default LoginPage;