package com.github.buyalsky.shoppingcartservice.service

import com.github.buyalsky.shoppingcartservice.entity.ShoppingCart

interface ShoppingCartService {
    suspend fun findById(id: String): ShoppingCart
    suspend fun findByIdCompleteInfo(id: String): ShoppingCart
    suspend fun createShoppingCart(customerId: String): ShoppingCart
    suspend fun deleteShoppingCart(customerId: String)
    suspend fun addProductToShoppingCart(customerId: String, productId: String, quantity: Int): ShoppingCart
    suspend fun subtractItemFromCart(customerId: String, productId: String, quantity: Int): ShoppingCart
    suspend fun removeItemFromCart(customerId: String, productId: String): ShoppingCart
}
