package com.github.buyalsky.productservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;


@Document
public class Product {
    @Id
    private String id;
    private String name;
    private String category;
    private Double price;
    private Integer numberOfItemsLeftInStock;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        Product product = (Product) o;
        return Objects.equals(id, product.id) && name.equals(product.name) && category.equals(product.category) && price.equals(product.price) && numberOfItemsLeftInStock.equals(product.numberOfItemsLeftInStock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, price, numberOfItemsLeftInStock);
    }
}
