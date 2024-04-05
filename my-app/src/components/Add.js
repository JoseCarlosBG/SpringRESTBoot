import React, { useState } from 'react';
import Header from './Header';
import axios from 'axios'; // Assuming you have Axios installed
import './styles/add.css';

const AddCouponPage = () => {
    // State variables to hold form data
    const [formData, setFormData] = useState({
        company: '',
        item: '',
        category: '',
        expiration: '',
        price: '',
        images: '',
        short: '',
        long: ''
    });

    // Function to handle form input changes
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    // Function to handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Send a POST request with the form data to your endpoint
            const response = await axios.post('http://localhost:8080/SpringRESTBoot/api/v1/gifts', formData);
            console.log('Coupon saved:', response.data);
            // Optionally, you can redirect the user to another page or show a success message
        } catch (error) {
            console.error('Error saving coupon:', error);
            // Handle error (e.g., display an error message to the user)
        }
    };

    return (
    <div>
        <section className="section_add">
            <h1 className="header-text">Add new coupon</h1>
            <form className="form" onSubmit={handleSubmit}>
                <fieldset className="addbox">
                    <div className="grid-newitem">
                        <div className="grid-company">
                            Company:
                            <p><input type="text" name="company" value={formData.company} onChange={handleInputChange} /></p>
                        </div>
                        <div className="grid-item">
                            Item name:
                            <p><input type="text" name="item" value={formData.item} onChange={handleInputChange} /></p>
                        </div>
                        <div className="grid-category">
                            Category:
                            <p>
                                <select className="categories" id="category" name="category" value={formData.category} onChange={handleInputChange}>
                                    <optgroup label="Group 1">
                                        <option value="Option 1.1">First</option>
                                        <option value="Option 1.2">Second</option>
                                    </optgroup>
                                    <optgroup label="Group 2">
                                        <option value="Option 2.1">First</option>
                                        <option value="Option 2.2">Second</option>
                                    </optgroup>
                                </select>
                            </p>
                        </div>
                        <div className="grid-expiration">
                            Valid to:
                            <p><input type="date" name="expiration" value={formData.expiration} onChange={handleInputChange} /></p>
                        </div>
                        <div className="grid-price">
                            Price:
                            <p><input type="text" name="price" value={formData.price} onChange={handleInputChange} /></p>
                        </div>
                        <div className="grid-images">
                            Images:
                            <p><input type="file" id="img" name="images" accept="image/*" onChange={handleInputChange} /></p>
                        </div>
                        <div className="grid-short">
                            Short description:
                            <p><textarea type="text" name="short" value={formData.short} onChange={handleInputChange} rows="4" cols="50"></textarea></p>
                        </div>
                        <div className="grid-long">
                            Long description:
                            <p><textarea type="text" name="long" value={formData.long} onChange={handleInputChange} rows="4" cols="50"></textarea></p>
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

export default AddCouponPage;
