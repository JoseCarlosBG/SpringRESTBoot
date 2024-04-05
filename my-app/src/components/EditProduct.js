import React, { useState, useEffect } from 'react';
import Header from './Header';
import axios from 'axios'; // Assuming you have Axios installed
import './styles/edit.css';

const EditProduct = ({ productId }) => {
    // State variables to hold form data and product details
    const [formData, setFormData] = useState({
        name: '',
        description: '',
        price: ''
    });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchProductDetails(productId);
    }, [productId]);

    // Function to fetch product details
    const fetchProductDetails = async (productId) => {
        try {
            const response = await axios.get(`http://localhost:8080/SpringRESTBoot/api/v1/gifts/${productId}`);
            const product = response.data; // Assuming the response contains product details
            setFormData({
                name: product.name,
                description: product.description,
                price: product.price
            });
            setLoading(false);
        } catch (error) {
            console.error('Error fetching product details:', error);
            setLoading(false);
        }
    };

    // Function to handle form input changes
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    // Function to handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Send a PUT request with the updated form data to your endpoint
            const response = await axios.put(`http://localhost:8080/SpringRESTBoot/api/v1/gifts/${productId}`, formData);
            console.log('Product updated:', response.data);
            // Optionally, you can redirect the user to another page or show a success message
        } catch (error) {
            console.error('Error updating product:', error);
            // Handle error (e.g., display an error message to the user)
        }
    };

    if (loading) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <Header />
            <section className="section_edit">
                <h1 className="header-text">Edit Product</h1>
                <form className="form" onSubmit={handleSubmit}>
                    <fieldset className="editbox">
                        <div className="grid-edit">
                            <div className="grid-name">
                                Name:
                                <p><input type="text" name="name" value={formData.name} onChange={handleInputChange} /></p>
                            </div>
                            <div className="grid-description">
                                Description:
                                <p><input type="text" name="description" value={formData.description} onChange={handleInputChange} /></p>
                            </div>
                            <div className="grid-price">
                                Price:
                                <p><input type="text" name="price" value={formData.price} onChange={handleInputChange} /></p>
                            </div>
                            <div className="grid-buttons">
                                <button type="button" className="btn btn-sm">Cancel</button>
                                <button type="submit" className="btn btn-sm">Save</button>
                            </div>
                        </div>
                    </fieldset>
                </form>
            </section>
        </div>
    );
};

export default EditProduct;
