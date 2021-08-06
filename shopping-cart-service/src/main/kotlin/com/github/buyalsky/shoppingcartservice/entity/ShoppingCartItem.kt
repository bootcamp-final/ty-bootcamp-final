package com.github.buyalsky.shoppingcartservice.entity

data class ShoppingCartItem(
        var productId: String,
        var quantity: Int,
        @Transient var price: Double = 0.0)
