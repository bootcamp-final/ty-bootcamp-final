package com.github.buyalsky.productservice.dto;

import java.util.Objects;

public class ProductStockChangeDto {
    private String productId;
    private Integer currentLeftInStock;

    public ProductStockChangeDto() {
    }

    public ProductStockChangeDto(String productId, Integer currentLeftInStock) {
        this.productId = productId;
        this.currentLeftInStock = currentLeftInStock;
    }

    public String getProductId() {
        return productId;
    }

    public Integer getCurrentLeftInStock() {
        return currentLeftInStock;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setCurrentLeftInStock(Integer currentLeftInStock) {
        this.currentLeftInStock = currentLeftInStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductStockChangeDto that = (ProductStockChangeDto) o;
        return productId.equals(that.productId) && currentLeftInStock.equals(that.currentLeftInStock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, currentLeftInStock);
    }
}
