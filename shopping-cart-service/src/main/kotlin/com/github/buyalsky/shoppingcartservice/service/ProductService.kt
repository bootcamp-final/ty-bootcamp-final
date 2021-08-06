package com.github.buyalsky.shoppingcartservice.service

import com.github.buyalsky.shoppingcartservice.entity.ProductContainedInCarts
import reactor.core.publisher.Mono

interface ProductService {
    suspend fun findById(productId: String): ProductContainedInCarts
    fun updateProductPrice(productId: String, price: Double)
    suspend fun getCartsThatFollowProduct(productId: String): Set<String>
    fun removeProduct(productId: String)
    suspend fun addFollowerToProduct(productId: String, userId: String): ProductContainedInCarts
}
