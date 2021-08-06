package com.github.buyalsky.shoppingcartservice.service

import com.github.buyalsky.shoppingcartservice.controller.ShoppingCartNotFoundException
import com.github.buyalsky.shoppingcartservice.dto.ProductInfoDto
import com.github.buyalsky.shoppingcartservice.entity.ShoppingCart
import com.github.buyalsky.shoppingcartservice.entity.ShoppingCartItem
import com.github.buyalsky.shoppingcartservice.repository.ShoppingCartRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class ShoppingCartServiceImpl(
    private val repository: ShoppingCartRepository,
    private val productService: ProductService,
    private val webClient: WebClient
): ShoppingCartService {
    @Value("\${product-service.url}")
    private lateinit var productServiceUrl: String

    override suspend fun findById(id: String) =
        repository.findById(id)?: throw ShoppingCartNotFoundException(id)

    override suspend fun findByIdCompleteInfo(id: String): ShoppingCart {
        val shipping = 5.5
        val (customerId, shoppingCartItems, _) = repository.findById(id)?: throw ShoppingCartNotFoundException(id)
        var subtotal = 0.0
        val newShoppingCartItems = shoppingCartItems.map {
            val productInfo = webClient.get()
                .uri("${productServiceUrl}/{productId}", it.productId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .awaitBody<ProductInfoDto>()

            ShoppingCartItem(it.productId, it.quantity, productInfo.price)
        }.toList()

        newShoppingCartItems.forEach {
            subtotal += it.price*it.quantity
        }
        println(newShoppingCartItems)
        println(subtotal)
        return ShoppingCart(customerId, newShoppingCartItems, shipping, subtotal, shipping+subtotal)
    }

    override suspend fun createShoppingCart(customerId: String): ShoppingCart {
        val shoppingCart = ShoppingCart.newShoppingCartWithId(customerId)
        return repository.save(shoppingCart)
    }

    override suspend fun deleteShoppingCart(customerId: String) =
        repository.deleteById(customerId)

    override suspend fun addProductToShoppingCart(customerId: String, productId: String, quantity: Int): ShoppingCart {
        val shoppingCart = findById(customerId)
        val shoppingCartItemForGivenProduct = shoppingCart.shoppingCartItems.find { i -> i.productId == productId }
        val newShoppingCartItems = shoppingCart.shoppingCartItems.filter { i -> i.productId != productId}.toMutableList()

        if (shoppingCartItemForGivenProduct == null) {
            newShoppingCartItems += ShoppingCartItem(productId, quantity)
            shoppingCart.shoppingCartItems = newShoppingCartItems
            productService.addFollowerToProduct(productId, customerId)
        } else {
            shoppingCartItemForGivenProduct.quantity += quantity
            newShoppingCartItems += shoppingCartItemForGivenProduct
            shoppingCart.shoppingCartItems = newShoppingCartItems
        }

        return repository.save(shoppingCart)
    }

    override suspend fun subtractItemFromCart(customerId: String, productId: String, quantity: Int): ShoppingCart {
        val shoppingCart = findById(customerId)
        val shoppingCartItem = shoppingCart.shoppingCartItems.find { item -> item.productId == productId }
        val newShoppingCartItems = shoppingCart.shoppingCartItems
            .filter { i -> i.productId != productId }.toMutableList()

        if (shoppingCartItem == null)
            return shoppingCart

        shoppingCartItem.quantity -= quantity
        if (shoppingCartItem.quantity <= 0) {
            shoppingCart.shoppingCartItems = shoppingCart.shoppingCartItems.filter { i -> i.productId != productId }
            return shoppingCart
        }

        newShoppingCartItems += shoppingCartItem
        shoppingCart.shoppingCartItems = newShoppingCartItems
        return repository.save(shoppingCart)
    }

    override suspend fun removeItemFromCart(customerId: String, productId: String): ShoppingCart {
        val shoppingCart = findById(customerId)
        shoppingCart.shoppingCartItems.filter { item -> item.productId != productId }
        return repository.save(shoppingCart)
    }

}
