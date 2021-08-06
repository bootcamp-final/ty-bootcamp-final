package com.github.buyalsky.shoppingcartservice.dto

data class ProductPriceChangeEmailDto(
    val productName: String,
    val userFullName: String,
    val userEmail: String,
    val previousPrice: Double,
    val currentPrice: Double,
)
