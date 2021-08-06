package com.github.buyalsky.shoppingcartservice.repository

import com.github.buyalsky.shoppingcartservice.entity.ShoppingCart
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ShoppingCartRepository: CoroutineCrudRepository<ShoppingCart, String>
