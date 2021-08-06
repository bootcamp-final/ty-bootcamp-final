package com.github.buyalsky.shoppingcartservice.dto

data class ProductInfoDto(
    val id: String,
    val name: String,
    val category: String,
    val price: Double,
    val numberOfItemsLeftInStock: Int
)
