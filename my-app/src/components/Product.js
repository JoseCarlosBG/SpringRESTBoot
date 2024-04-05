import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Product = () => {
    const [products, setProducts] = useState([]);
    const [selectedProduct, setSelectedProduct] = useState(null);
    const [editedProduct, setEditedProduct] = useState(null);
    const [formData, setFormData] = useState({
        name: '',
        description: '',
        price: ''
    });
    const [showAddForm, setShowAddForm] = useState(false);

    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = async () => {
        try {
            const response = await axios.get('http://localhost:8080/SpringRESTBoot/api/v1/gifts/1');
            if (!response.data || !response.data._embedded || !response.data._embedded.giftCertificateList) {
                throw new Error('Invalid response structure');
            }
            setProducts(response.data._embedded.giftCertificateList);
        } catch (error) {
            console.error('Error fetching products:', error);
        }
    };

    const handleViewDetails = (product) => {
        setSelectedProduct(selectedProduct === product ? null : product);
    };

    const handleEdit = (product) => {
        setEditedProduct(product);
    };

    const handleDelete = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/SpringRESTBoot/api/v1/gifts/${id}`);
            fetchProducts(); // Refresh product list
        } catch (error) {
            console.error('Error deleting product:', error);
        }
    };

    const handleFormInputChange = (e) => {
        const { name, value } = e.target;
        setEditedProduct({ ...editedProduct, [name]: value });
    };

    const handleFormSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.put(`http://localhost:8080/SpringRESTBoot/api/v1/gifts/${editedProduct.id}`, editedProduct);
            if (response.status === 204) {
                // Update successful
                setEditedProduct(null);
                fetchProducts(); // Refresh product list
            }
        } catch (error) {
            console.error('Error updating product:', error);
        }
    };

    const handleAddNew = async () => {
        try {
            const response = await axios.post('http://localhost:8080/SpringRESTBoot/api/v1/gifts/', formData);
            if (response.status === 201) {
                // Creation successful
                setFormData({ name: '', description: '', price: '' }); // Clear form fields
                fetchProducts(); // Refresh product list
                setShowAddForm(false); // Close the form
            }
        } catch (error) {
            console.error('Error adding product:', error);
        }
    };

    const handleNewFormInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    return (
        <div>
            <h2>Product List</h2>
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Price</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {products.map(product => (
                        <tr key={product.id}>
                            <td>{product.name}</td>
                            <td>{product.description}</td>
                            <td>{product.price}</td>
                            <td>
                                <button onClick={() => handleViewDetails(product)}>
                                    {selectedProduct === product ? 'Close' : 'View Details'}
                                </button>
                                <button onClick={() => handleEdit(product)}>Edit</button>
                                <button onClick={() => handleDelete(product.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            {selectedProduct && (
                <div>
                    <h2>Product Details</h2>
                    <div>Name: {selectedProduct.name}</div>
                    <div>Description: {selectedProduct.description}</div>
                    <div>Price: {selectedProduct.price}</div>
                </div>
            )}
            {editedProduct && (
                <div>
                    <h2>Edit Product</h2>
                    <form onSubmit={handleFormSubmit}>
                        <input type="text" name="name" value={editedProduct.name} onChange={handleFormInputChange} />
                        <input type="text" name="description" value={editedProduct.description} onChange={handleFormInputChange} />
                        <input type="number" name="price" value={editedProduct.price} onChange={handleFormInputChange} />
                        <button type="submit">Save Changes</button>
                    </form>
                </div>
            )}
            {showAddForm && (
                <div>
                    <h2>Add New Product</h2>
                    <form onSubmit={handleAddNew}>
                        <input type="text" name="name" value={formData.name} onChange={handleNewFormInputChange} placeholder="Name" required />
                        <input type="text" name="description" value={formData.description} onChange={handleNewFormInputChange} placeholder="Description" required />
                        <input type="number" name="price" value={formData.price} onChange={handleNewFormInputChange} placeholder="Price" required />
                        <button type="submit">Add Product</button>
                        <button type="button" onClick={() => setShowAddForm(false)}>Close</button>
                    </form>
                </div>
            )}
            <button onClick={() => setShowAddForm(true)}>Add New</button>
        </div>
    );
};

export default Product;
