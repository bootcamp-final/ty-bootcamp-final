package com.github.buyalsky.productservice.controller;

import com.github.buyalsky.productservice.dto.UpdateProductDto;
import com.github.buyalsky.productservice.entity.Product;
import com.github.buyalsky.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Iterable<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable String productId) {
        return productService.getProductById(productId);
    }

    @PostMapping
    public Product addNewProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PutMapping("/{productId}")
    public Product updateProduct(@PathVariable String productId,
                                 @RequestBody UpdateProductDto updateProductDto) {
        return productService.updateProduct(productId, updateProductDto);
    }
}
