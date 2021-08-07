package com.github.buyalsky.productservice.service;

import com.github.buyalsky.productservice.dto.ProductPriceChangeDto;
import com.github.buyalsky.productservice.dto.ProductStockChangeDto;
import com.github.buyalsky.productservice.dto.UpdateProductDto;
import com.github.buyalsky.productservice.entity.Product;
import com.github.buyalsky.productservice.exception.ProductNotFoundException;
import com.github.buyalsky.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Value("${stock-count-notification-threshold}")
    private int stockCountNotificationThreshold;

    private final ProductRepository productRepository;
    private final KafkaProducerService producerService;

    public ProductServiceImpl(ProductRepository productRepository, KafkaProducerService producerService) {
        this.productRepository = productRepository;
        this.producerService = producerService;
    }

    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                    new ProductNotFoundException(
                        String.format("Product with given id (%s) cannot found", id)
                    ));
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
    }

    public Product updateProduct(String productId, UpdateProductDto updateProductDto) {
        Optional<Product> currentProductOptional = productRepository.findById(productId);
        if (currentProductOptional.isEmpty())
            throw new ProductNotFoundException("Specified product does not exist hence cannot be updated");

        Product currentProduct = currentProductOptional.get();

        Double previousPrice = currentProduct.getPrice();
        Double price = updateProductDto.getPrice();
        Integer numberOfItemsLeftInStock = updateProductDto.getNumberOfItemsLeftInStock();

        if (price != null) {
            checkProductPrice(productId, previousPrice, price);
        }
        if (numberOfItemsLeftInStock != null) {
            checkProductStock(productId, numberOfItemsLeftInStock);
        }

        Product updatedProduct = updateProductDto.mapToProduct(currentProduct);

        return productRepository.save(updatedProduct);
    }

    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    private void checkProductPrice(String productId, Double previousPrice, Double newPrice) {
        if (newPrice >= previousPrice)
            return;
        ProductPriceChangeDto priceChangeDto = new ProductPriceChangeDto(productId, previousPrice, newPrice);
        this.producerService.sendPriceChangeEvent(priceChangeDto);
    }

    private void checkProductStock(String productId, Integer numberOfItemsLeft) {
        if (numberOfItemsLeft > stockCountNotificationThreshold)
            return;
        ProductStockChangeDto stockChangeDto = new ProductStockChangeDto(productId, numberOfItemsLeft);
        this.producerService.sendStockChangeEvent(stockChangeDto);
    }

    public void setStockCountNotificationThreshold(int stockCountNotificationThreshold) {
        this.stockCountNotificationThreshold = stockCountNotificationThreshold;
    }
}
