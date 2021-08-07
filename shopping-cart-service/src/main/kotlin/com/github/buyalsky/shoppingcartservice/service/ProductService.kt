package com.github.buyalsky.shoppingcartservice.service

import com.github.buyalsky.shoppingcartservice.entity.ProductContainedInCarts
import reactor.core.publisher.Mono

interface ProductService {
    suspend fun findById(productId: String): ProductContainedInCarts
    suspend fun getCartsThatFollowProduct(productId: String): Set<String>
    suspend fun removeProduct(productId: String)
    suspend fun addFollowerToProduct(productId: String, userId: String): ProductContainedInCarts
}
