import React, { useState } from 'react';
import './styles/main.css';

const Header = ({ isLoggedIn, onLogout, onSearch, searchTerm, handleSearchInputChange, handleSearchSubmit }) => {
    const [selectedCategory, setSelectedCategory] = useState('');

    const handleLogoutClick = () => {
        onLogout();
    };

    
    return (
        <header className="nav">
            <div className="menu-logo">
                <div className="menu-icon">&#9776;</div>
                <div className="logo">Logo</div>
            </div>
            <form className="search-box" onSubmit={handleSearchSubmit}>
                <input type="text" id="searchInput" placeholder="Search..." value={searchTerm} onChange={handleSearchInputChange} />
                <select value={selectedCategory} onChange={(event) => setSelectedCategory(event.target.value)}>
                    <option value="">All categories</option>
                    <option value="Category 1">Category 1</option>
                    <option value="Category 2">Category 2</option>
                    <option value="Category 3">Category 3</option>
                </select>
                <button type="submit">Search</button>
            </form>
            <div className="auth-buttons">
                <div className="icons">
                    <span className="heart-icon">&#10084;</span>
                    <span className="cart-icon">&#128722;</span>
                </div>
                {isLoggedIn ? (
                    <button className="logout" onClick={handleLogoutClick}>LOGOUT</button>
                ) : (
                    <React.Fragment>
                        <button className="login">LOGIN</button>
                        <button className="signup">SIGN UP</button>
                    </React.Fragment>
                )}
            </div>
        </header>
    );
};

export default Header;
