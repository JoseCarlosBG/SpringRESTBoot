import React from 'react';
import Header from './Header'; // Import the Header component
import './styles/details.css'; // Import the CSS file for styling

const DetailsPage = () => {
    return (
        <div>
            <Header /> {/* Include the Header component */}
            <div className="row">
                <div className="column image">
                    <img src="image.jpg" alt="Image" />
                </div>
                <div className="column product-card">
                    <h1>Coupon name</h1>
                    <p>
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Eget est lorem ipsum dolor sit amet consectetur.
                    </p>
                    <div className="details">
                        <p className="price">$19.99</p>
                        <p>
                            <a href="">Add to Cart</a>
                            <span className="cart-icon">&#128722;</span>
                        </p>
                    </div>
                </div>
                <div className="column product-details">
                    <h2>Item detailed description</h2>
                    <p>
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Eget est lorem ipsum dolor sit amet consectetur. Ante metus dictum at tempor commodo ullamcorper a. Elementum curabitur vitae nunc sed velit. Diam donec adipiscing tristique risus nec feugiat in fermentum. Ultrices in iaculis nunc sed augue. Mauris commodo quis imperdiet massa tincidunt nunc pulvinar sapien et. Est pellentesque elit ullamcorper dignissim cras tincidunt lobortis feugiat vivamus. Nunc congue nisi vitae suscipit tellus. Duis convallis convallis tellus id interdum velit laoreet. Velit egestas dui id ornare arcu odio ut sem nulla. Fermentum odio eu feugiat pretium nibh ipsum consequat nisl. Tincidunt id aliquet risus feugiat in ante. Augue ut lectus arcu bibendum at varius. Massa tincidunt dui ut ornare lectus sit amet.
                    </p>
                </div>
            </div>
        </div>
    );
};

export default DetailsPage;
