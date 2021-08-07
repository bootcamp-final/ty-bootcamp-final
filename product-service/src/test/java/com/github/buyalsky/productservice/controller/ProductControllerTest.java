package com.github.buyalsky.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.buyalsky.productservice.dto.UpdateProductDto;
import com.github.buyalsky.productservice.entity.Product;
import com.github.buyalsky.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private Product mockProduct() {
        Product product = new Product();
        product.setPrice(5.5);
        product.setCategory("acategory");
        product.setNumberOfItemsLeftInStock(10);
        product.setName("aproduct");
        return product;
    }

    private Product mockProductWithId(String id) {
        Product product = mockProduct();
        product.setId(id);
        return product;
    }

    @Test
    void getAllProducts() throws Exception {
        //given
        String firstProductId = "ANID";
        String secondProductId = "ANID2";

        Mockito.when(productService.getAllProducts())
            .thenReturn(Arrays.asList(
                mockProductWithId(firstProductId),
                mockProductWithId(secondProductId)
            ));

        //when-then
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().json("[{}, {}]"));
    }

    @Test
    void getProductById() throws Exception {
        //given
        String productId = "AN_ID";
        Product product = mockProduct();
        Product savedProduct = mockProductWithId(productId);

        Mockito.when(productService.addProduct(product)).thenReturn(savedProduct);

        //when-then
        mockMvc.perform(get("/{id}", productId))
            .andExpect(status().isOk());
    }

    @Test
    void addNewProduct() throws Exception {
        //given
        String productId = "AN_ID";
        Product product = mockProduct();
        Product savedProduct = mockProductWithId(productId);

        Mockito.when(productService.addProduct(product)).thenReturn(savedProduct);

        //when-then
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(productId));
    }

    @Test
    void updateProduct() throws Exception {
        String productId = "AN_ID";
        Product savedProduct = mockProductWithId(productId);
        Mockito.when(productService.getProductById(productId)).thenReturn(savedProduct);

        Double newPrice = 2.2;
        String newCategory = "newcategory";
        Integer newStockCount = 100;
        UpdateProductDto updateProductDto = new UpdateProductDto.Builder()
            .price(newPrice)
            .category(newCategory)
            .numberOfItemsLeftInStock(newStockCount).build();

        Product expectedProduct = new Product();
        expectedProduct.setId(productId);
        expectedProduct.setName(mockProduct().getName());
        expectedProduct.setPrice(newPrice);
        expectedProduct.setNumberOfItemsLeftInStock(newStockCount);
        expectedProduct.setCategory(newCategory);

        Mockito.when(productService.updateProduct(productId, updateProductDto))
            .thenReturn(expectedProduct);

        //when-then
        mockMvc.perform(put("/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(updateProductDto))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(productId))
            .andExpect(jsonPath("$.category").value(newCategory))
            .andExpect(jsonPath("$.price").value(newPrice))
            .andExpect(jsonPath("$.numberOfItemsLeftInStock").value(newStockCount));
    }
}
