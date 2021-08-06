package com.github.buyalsky.shoppingcartservice.service

import com.github.buyalsky.shoppingcartservice.controller.ProductNotFoundException
import com.github.buyalsky.shoppingcartservice.entity.ProductContainedInCarts
import com.github.buyalsky.shoppingcartservice.repository.ProductRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository
) : ProductService {
    override suspend fun findById(productId: String): ProductContainedInCarts {
        return productRepository.findById(productId)?: throw ProductNotFoundException(productId)
    }

    override fun updateProductPrice(productId: String, price: Double) {
        TODO("Not yet implemented")
    }

    override suspend fun getCartsThatFollowProduct(productId: String): Set<String> {
        return findById(productId).cartIdsThatContainProduct
    }

    override fun removeProduct(productId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun addFollowerToProduct(productId: String, userId: String): ProductContainedInCarts {
        println("deneme")
        val foundProduct: ProductContainedInCarts = try {
            findById(productId)
        } catch (e: ProductNotFoundException) {
            ProductContainedInCarts(productId, hashSetOf(userId))
        }
        foundProduct.cartIdsThatContainProduct += userId
        return productRepository.save(foundProduct)
    }
}
