package com.github.buyalsky.shoppingcartservice.controller

import com.github.buyalsky.shoppingcartservice.dto.AddToCartDto
import com.github.buyalsky.shoppingcartservice.dto.SubtractFromCartDto
import com.github.buyalsky.shoppingcartservice.entity.ProductContainedInCarts
import com.github.buyalsky.shoppingcartservice.entity.ShoppingCart
import com.github.buyalsky.shoppingcartservice.service.ProductService
import com.github.buyalsky.shoppingcartservice.service.ShoppingCartService
import org.springframework.web.bind.annotation.*

@RestController
class ShoppingCartController(
    private val service: ShoppingCartService,
    private val productService: ProductService
) {

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: String): ShoppingCart =
            service.findById(id)


    @GetMapping("/product/{id}")
    suspend fun getByIdProduct(@PathVariable id: String): ProductContainedInCarts =
            productService.findById(id)


    @PostMapping("/{customerId}")
    suspend fun create(@PathVariable customerId: String): ShoppingCart =
            service.createShoppingCart(customerId)


    @PutMapping("/{customerId}/add-to-cart")
    suspend fun addProductToCart(@PathVariable customerId: String,
                                 @RequestBody addToCartDto: AddToCartDto): ShoppingCart =
        service.addProductToShoppingCart(customerId, addToCartDto.productId, addToCartDto.quantity)


    @PutMapping("/{customerId}/substract-from-cart")
    suspend fun subtractProductFromCart(@PathVariable customerId: String,
                                 @RequestBody subtractFromCartDto: SubtractFromCartDto): ShoppingCart =
        service.addProductToShoppingCart(customerId, subtractFromCartDto.productId, subtractFromCartDto.quantity)


}
