package com.github.buyalsky.productservice.dto;

import com.github.buyalsky.productservice.entity.Product;

import java.util.Objects;

public class UpdateProductDto {
    private String name;
    private String category;
    private Double price;
    private Integer numberOfItemsLeftInStock;

    public UpdateProductDto() {}

    public UpdateProductDto(String name, String category, Double price, Integer numberOfItemsLeftInStock) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.numberOfItemsLeftInStock = numberOfItemsLeftInStock;
    }

    public Product mapToProduct(Product product) {
        if (this.name != null)
            product.setName(this.name);
        if (this.category != null)
            product.setCategory(this.category);
        if (this.price != null)
            product.setPrice(this.price);
        if (this.numberOfItemsLeftInStock != null)
            product.setNumberOfItemsLeftInStock(this.numberOfItemsLeftInStock);

        return product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNumberOfItemsLeftInStock() {
        return numberOfItemsLeftInStock;
    }

    public void setNumberOfItemsLeftInStock(Integer numberOfItemsLeftInStock) {
        this.numberOfItemsLeftInStock = numberOfItemsLeftInStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateProductDto that = (UpdateProductDto) o;
        return Objects.equals(name, that.name) && Objects.equals(category, that.category) && Objects.equals(price, that.price) && Objects.equals(numberOfItemsLeftInStock, that.numberOfItemsLeftInStock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, price, numberOfItemsLeftInStock);
    }

    public static class Builder {
        private String name;
        private String category;
        private Double price;
        private Integer numberOfItemsLeftInStock;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder numberOfItemsLeftInStock(Integer numberOfItemsLeftInStock) {
            this.numberOfItemsLeftInStock=numberOfItemsLeftInStock;
            return this;
        }

        public UpdateProductDto build() {
            return new UpdateProductDto(name, category, price, numberOfItemsLeftInStock);
        }
    }
}
