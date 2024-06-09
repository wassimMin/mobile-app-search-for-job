import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from "axios";

const AddUserPage = () => {
    const navigate = useNavigate();
    const [userData, setUserData] = useState({
        name: '',
        email: '',
        password: '',
        userType: ''
    });
    const handleChange = (event) => {
        const { name, value } = event.target;
        setUserData((prevData) => ({
            ...prevData,
            [name]: value
        }));
    };
    const handleSubmit = async (event) => {
        event.preventDefault();
        
        const { name, email, password, userType } = userData;
    
        try {
            const response = await axios.post('http://localhost:5000/api/users/adduser', {
                name,
                email,
                password,
                userType
            });
    
            console.log('User added successfully:', response.data);
            navigate('/ControlePanel');
        } catch (error) {
            console.error('Error adding user:', error);
        }
    };
    return (
        <div className="flex items-center justify-center h-screen">
            <div className="max-w-md w-full px-4 py-8 bg-gray-800 rounded-lg shadow-md">
                <h2 className="text-2xl font-semibold mb-4 text-center text-white">Add New User</h2>
                <form className="space-y-6" onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="name" className="block text-sm font-medium text-gray-200">
                            Name
                        </label>
                        <input
                            type="text"
                            name="name"
                            id="name"
                            value={userData.name}
                            onChange={handleChange}
                            className="mt-1 p-2.5 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring focus:ring-primary-500 focus:ring-opacity-50 bg-gray-700 text-gray-200"
                            placeholder="Enter name"
                        />
                    </div>
                    <div>
                        <label htmlFor="email" className="block text-sm font-medium text-gray-200">
                            Email
                        </label>
                        <input
                            type="email"
                            name="email"
                            id="email"
                            value={userData.email}
                            onChange={handleChange}
                            className="mt-1 p-2.5 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring focus:ring-primary-500 focus:ring-opacity-50 bg-gray-700 text-gray-200"
                            placeholder="Enter email"
                        />
                    </div>
                    <div>
                        <label htmlFor="password" className="block text-sm font-medium text-gray-200">
                            Password
                        </label>
                        <input
                            type="password"
                            name="password"
                            id="password"
                            value={userData.password}
                            onChange={handleChange}
                            className="mt-1 p-2.5 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring focus:ring-primary-500 focus:ring-opacity-50 bg-gray-700 text-gray-200"
                            placeholder="Enter password"
                        />
                    </div>
                    <div>
                        <label htmlFor="userType" className="block text-sm font-medium text-gray-200">
                            User Type
                        </label>
                        <select
                            id="userType"
                            name="userType"
                            value={userData.userType}
                            onChange={handleChange}
                            className="mt-1 p-2.5 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring focus:ring-primary-500 focus:ring-opacity-50 bg-gray-700 text-gray-200"
                        >
                            <option value="">Select user type</option>
                            <option value="company">Company</option>
                            <option value="jobSearcher">Job Searcher</option>
                        </select>
                    </div>
                    <div className="flex justify-center">
                        <button
                            type="submit"
                            className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800"
                        >
                            Add User
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default AddUserPage;
