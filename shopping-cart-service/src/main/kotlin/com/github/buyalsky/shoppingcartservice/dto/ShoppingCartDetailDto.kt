package com.github.buyalsky.shoppingcartservice.dto

data class ShoppingCartDetailDto(
    val isEliteMember: Boolean,
    val totalAmount: Double,
    val categories: List<String>
)
