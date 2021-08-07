package com.github.buyalsky.productservice.dto;

import java.util.Objects;

public class ProductPriceChangeDto {
    private String productId;
    private Double previousPrice;
    private Double newPrice;

    public ProductPriceChangeDto() {
    }

    public ProductPriceChangeDto(String productId, Double previousPrice, Double newPrice) {
        this.productId = productId;
        this.previousPrice = previousPrice;
        this.newPrice = newPrice;
    }

    public String getProductId() {
        return productId;
    }

    public Double getPreviousPrice() {
        return previousPrice;
    }

    public Double getNewPrice() {
        return newPrice;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setPreviousPrice(Double previousPrice) {
        this.previousPrice = previousPrice;
    }

    public void setNewPrice(Double newPrice) {
        this.newPrice = newPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPriceChangeDto that = (ProductPriceChangeDto) o;
        return productId.equals(that.productId) && previousPrice.equals(that.previousPrice) && newPrice.equals(that.newPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, previousPrice, newPrice);
    }
}
