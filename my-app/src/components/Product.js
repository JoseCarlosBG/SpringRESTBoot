import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Product = ({ productList, searchTerm }) => {
    const [products, setProducts] = useState([]);
    const [selectedProduct, setSelectedProduct] = useState(null);
    const [editedProduct, setEditedProduct] = useState(null);
    const [formData, setFormData] = useState({
        name: '',
        description: '',
        price: '',
        duration: ''
    });
    const [showAddForm, setShowAddForm] = useState(false);
    const [currentPage, setCurrentPage] = useState(1);
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
        fetchProducts(currentPage); // Fetch products for the current page and search term
    }, [currentPage, searchTerm]); // Fetch products whenever currentPage or searchTerm changes

    const handleViewDetails = (product) => {
        setSelectedProduct(selectedProduct === product ? null : product);
    };

    const handleEdit = (product) => {
        setEditedProduct(product);
	fetchProducts(currentPage); // Refresh product list
    };

    const handleDelete = async (id) => {
        const confirmDelete = window.confirm('Are you sure you want to delete this product?');
        if (confirmDelete) {
            try {
                await axios.delete(`http://localhost:8080/SpringRESTBoot/api/v1/gifts/${id}`);
		if (currentPage > totalPages - 1) {
                	// If so, set the current page to the last page
                	setCurrentPage(totalPages - 1);
            	}
		fetchProducts(currentPage); // Refresh product list
            } catch (error) {
                console.error('Error deleting product:', error);
            }
        }
    };


    const handleFormInputChange = (e) => {
        const { name, value } = e.target;
        setEditedProduct({ ...editedProduct, [name]: value });
    };

    const handleFormSubmit = async (e) => {
        e.preventDefault();
	const parsedPrice = parseFloat(editedProduct.price);
    	if (isNaN(parsedPrice) || parsedPrice <= 0) {
            console.error('Invalid price entered');
            return;
    	}
        try {
            const response = await axios.put(`http://localhost:8080/SpringRESTBoot/api/v1/gifts/${editedProduct.id}`, editedProduct);
            if (response.status === 204) {
                // Update successful
                setEditedProduct(null);
                fetchProducts(currentPage); // Refresh product list
            }
        } catch (error) {
            console.error('Error updating product:', error);
        }
    };

    const handleAddNew = async () => {
        const parsedPrice = parseFloat(formData.price);
    	if (isNaN(parsedPrice) || parsedPrice <= 0) {
            console.error('Invalid price entered');
            return;
    	}
	try {
            const response = await axios.post('http://localhost:8080/SpringRESTBoot/api/v1/gifts/', formData);
            if (response.status === 201) {
                // Creation successful
                setFormData({ name: '', description: '', price: '', duration: '' }); // Clear form fields
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

    const handlePageChange = (pageNumber) => {
        if (pageNumber < 1) {
            pageNumber = 1;
        } else if (pageNumber > totalPages) {
            pageNumber = totalPages;
        }
        setCurrentPage(pageNumber); // Update currentPage state
    };

    const handlePageInputChange = (e) => {
        let pageNumber = parseInt(e.target.value);
        if (!isNaN(pageNumber)) {
            handlePageChange(pageNumber);
        }
    };

// Calculate the width of the input field based on the length of the current page number
    const inputWidth = `${Math.max(currentPage.toString().length * 10, 30)}px`;
    return (
        <div>
            <h2>Product List</h2>
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Price</th>
                        <th>Duration</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {products.map(product => (
                        <tr key={product.id}>
                            <td>{product.name}</td>
                            <td>{product.description}</td>
                            <td>{product.price.toFixed(2)}</td>
                            <td>{product.duration}</td>
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
                    <div>Price: {selectedProduct.price.toFixed(2)}</div>
                    <div>Duration: {selectedProduct.duration}</div>
                </div>
            )}
            {editedProduct && (
                <div>
                    <h2>Edit Product</h2>
                    <form onSubmit={handleFormSubmit}>
                        <label htmlFor="name">Name:</label>
                        <input type="text" name="name" value={editedProduct.name} onChange={handleFormInputChange} />
                        <label htmlFor="description">Description:</label>
                        <input type="text" name="description" value={editedProduct.description} onChange={handleFormInputChange} />
                        <label htmlFor="price">Price:</label>
                        <input type="text" name="price" value={editedProduct.price.toFixed(2)} onChange={handleFormInputChange} />
                        <label htmlFor="duration">Duration:</label>
                        <input type="number" name="duration" value={editedProduct.duration} onChange={handleFormInputChange} />
                        <button type="submit">Save Changes</button>
                        <button type="button" onClick={() => setEditedProduct(null)}>Cancel</button>
                    </form>
                </div>
            )}
            {showAddForm && (
                <div>
                    <h2>Add New Product</h2>
                    <form onSubmit={handleAddNew}>
                        <label htmlFor="name">Name:</label>
                        <input type="text" name="name" value={formData.name} onChange={handleNewFormInputChange} placeholder="Name" required />
                        <label htmlFor="description">Description:</label>
                        <input type="text" name="description" value={formData.description} onChange={handleNewFormInputChange} placeholder="Description" required />
                        <label htmlFor="price">Price:</label>
                        <input type="text" name="price" value={formData.price} onChange={handleNewFormInputChange} placeholder="Price" required />
                        <label htmlFor="duration">Duration:</label>
                        <input type="number" name="duration" value={formData.duration} onChange={handleNewFormInputChange} placeholder="Duration" required />
                        <button type="submit">Add Product</button>
                        <button type="button" onClick={() => setShowAddForm(false)}>Close</button>
                    </form>
                </div>
            )}

            <div>
                {/* Pagination buttons */}
                <button onClick={() => handlePageChange(1)} disabled={!paginationLinks.find(item => item.rel === 'first')}>First</button>
                <button onClick={() => handlePageChange(currentPage - 1)} disabled={!paginationLinks.find(item => item.rel === 'prev')}>Previous</button>
                <input 
                    type="text" 
                    value={currentPage} 
                    onChange={handlePageInputChange} 
                    style={{ width: inputWidth }} // Set the width dynamically
                />
                <button onClick={() => handlePageChange(currentPage + 1)} disabled={!paginationLinks.find(item => item.rel === 'next')}>Next</button>
                <button onClick={() => handlePageChange(totalPages)} disabled={!paginationLinks.find(item => item.rel === 'last')}>Last</button>
		<button onClick={() => setShowAddForm(true)}>Add Product</button> {/* Add button */}
            </div>
        </div>
    );
};

export default Product;
