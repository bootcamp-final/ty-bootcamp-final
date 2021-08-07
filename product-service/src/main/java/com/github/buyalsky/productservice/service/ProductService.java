package com.github.buyalsky.productservice.service;

import com.github.buyalsky.productservice.dto.UpdateProductDto;
import com.github.buyalsky.productservice.entity.Product;

public interface ProductService {
    public Product getProductById(String id);
    public Product addProduct(Product product);
    public void deleteProduct(String productId);
    public Product updateProduct(String productId, UpdateProductDto updateProductDto);
    public Iterable<Product> getAllProducts();
}
