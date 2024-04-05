import React from 'react';
import Header from './Header'; // Import the Header component
import './styles/checkout.css'; // Import the CSS file for styling

const CheckoutPage = () => {
    return (
        <div>
            <Header /> {/* Include the Header component */}
            <header className="header">
                <h2>Checkout</h2>
            </header>
            <div className="checkoutbox">
                <div className="product-list-container">
                    <div className="product-list">
                        <div className="column image">
                            <img src="image.jpg" alt="Image" />
                        </div>
                        <div className="column details">
                            <h2>Coupon name</h2>
                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
                        </div>
                        <div className="column price">
                            <p className="price">$19.99</p>
                        </div>
                    </div>
                    {/* Repeat the product list as needed */}
                    <div className="product-list">
                        {/* Product list item */}
                    </div>
                    {/* Add more product list items as needed */}
                    <div className="product-list">
                        <div className="column blank"></div>
                        <div className="column total">
                            <h2>Total</h2>
                        </div>
                        <div className="column price">
                            <p className="price">$39.96</p>
                        </div>
                    </div>
                </div>
                <div className="align-right">
                    <button>Cancel</button>
                    <button>Checkout</button>
                </div>
            </div>
        </div>
    );
};

export default CheckoutPage;
