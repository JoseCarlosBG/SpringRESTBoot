import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios'; // Assuming you have Axios installed

import Header from './components/Header';
import LoginPage from './components/LoginPage';
import Product from './components/Product'; // Assuming you have a Product component
import CheckoutPage from './components/CheckoutPage'; // Assuming you have a CheckoutPage component
import DetailsPage from './components/DetailsPage'; // Assuming you have a DetailsPage component
import RegisterPage from './components/RegisterPage'; // Assuming you have a RegisterPage component
import Add from './components/Add'; // Assuming you have an Add component

import './components/styles/main.css';

const App = () => {
    const location = useLocation();
    const [currentPage, setCurrentPage] = useState('home');
    const [isLoggedIn, setIsLoggedIn] = useState(false); // Set isLoggedIn to false initially
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [products, setProducts] = useState([]);
    const [showAddCouponPage, setShowAddCouponPage] = useState(false);
    const [searchResults, setSearchResults] = useState(null); // State to store search results
    const [searchTerm, setSearchTerm] = useState('');
    const [totalPages, setTotalPages] = useState(1);
    const [paginationLinks, setPaginationLinks] = useState([]);
    
    const fetchProducts = async (pageNumber) => {
    try {
        let url;
        if (searchTerm) {
            // If searchTerm exists, fetch products with searchTerm
            url = `http://localhost:8080/SpringRESTBoot/api/v1/gifts/searchByName/${searchTerm}/${pageNumber}`;
        } else {
            // If searchTerm is empty or null, fetch products without searchTerm
            url = `http://localhost:8080/SpringRESTBoot/api/v1/gifts/${pageNumber}`;
        }
        
        const response = await axios.get(url);
        const responseData = response.data;

        if (!responseData || !responseData.giftResources || !responseData.totalPages || !responseData.links) {
            throw new Error('Invalid response structure');
        }

        const { giftResources, totalPages, links } = responseData;

        setProducts(giftResources);
        setTotalPages(totalPages);
        setPaginationLinks(links);
    } catch (error) {
        console.error('Error fetching products:', error);
    }
    };



    useEffect(() => {
        // Extract the path from the location object
        const path = location.pathname;

        // Determine the currentPage based on the path
        switch (path) {
            case '/add':
                setCurrentPage('add');
                break;
            case '/checkout':
                setCurrentPage('checkout');
                break;
            case '/details':
                setCurrentPage('details');
                break;
            case '/register':
                setCurrentPage('register');
                break;
            case '/certificates':
                setCurrentPage('certificates');
                break;
            default:
                setCurrentPage('login');
        }
    }, [location.pathname]);

    // Function to handle search
const handleSearch = async (searchTerm) => {
    try {
        // Send a request to your backend to search for products based on the provided search term
        // Replace 'http://localhost:8080' with your backend URL
        const response = await axios.get(`http://localhost:8080/SpringRESTBoot/api/v1/gifts/searchByName/${searchTerm}/1`);

        // Assuming your backend returns the search results in the response data
        const searchResults = response.data;
        //console.log(response.data);

        // Update the state with the search results
        setSearchResults(searchResults);
    } catch (error) {
        // Handle search error
        console.error('Search failed:', error.response.data.message);
    }
};

// Function to handle search input change
const handleSearchInputChange = (event) => {    setSearchTerm(event.target.value); // Update searchTerm state variable
};

// Function to handle search form submit
const handleSearchSubmit = (event) => {
    event.preventDefault();
    // Call handleSearch with the current value of searchTerm
    handleSearch(searchTerm);
};

    const handleLogin = async () => {
        try {
            // Send POST request to your backend for authentication
            const response = await axios.post('http://localhost:8080/SpringRESTBoot/api/v1/auth/login', {
                username,
                password
            });

            // Assuming your backend returns a token upon successful authentication
            const token = response.data.token;

            // Store the token securely, e.g., in localStorage
            localStorage.setItem('token', token);

            // Update isLoggedIn state
            setIsLoggedIn(true);

            // Implement further logic as needed, e.g., redirect to another page or update state
            console.log('Login successful!');
        } catch (error) {
            // Handle login error
            console.error('Login failed:', error.response.data.message);
        }
    };

    const handleLogout = () => {
        // Clear token from localStorage or perform any other logout logic
        localStorage.removeItem('token');
        // Update isLoggedIn state
        setIsLoggedIn(false);
    };

    const scrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    };

    const renderPage = () => {
    switch (currentPage) {
        case 'add':
            return <Add />;
        case 'checkout':
            return <CheckoutPage />;
        case 'details':
            return <DetailsPage />;
        case 'register':
            return <RegisterPage />;
        case 'certificates':
            return <Product products={searchResults || []} fetchProducts={fetchProducts} searchTerm={searchTerm} />;
        case 'login':
            return <LoginPage onLogin={handleLogin} />;
        default:
            return <LoginPage onLogin={handleLogin} />;
    }
};

    return (
        <div>
            <Header isLoggedIn={isLoggedIn} onLogout={handleLogout} onSearch={handleSearch} searchTerm={searchTerm} handleSearchInputChange={handleSearchInputChange} handleSearchSubmit={handleSearchSubmit} />
            {renderPage()}
        </div>
    );
};

export default App;
