import React from 'react';
import './styles/main.css';

const Header = ({ isLoggedIn, onLogout }) => {
    const handleLogoutClick = () => {
        onLogout();
    };

    return (
        <header className="nav">
            <div className="menu-logo">
                <div className="menu-icon">&#9776;</div>
                <div className="logo">Logo</div>
            </div>
            <div className="search-box">
                <input type="text" id="searchInput" placeholder="Search..." />
                <select>
                    <option value="" disabled selected>All categories</option>
                    <option>Category 1</option>
                    <option>Category 2</option>
                    <option>Category 3</option>
                </select>
            </div>
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
