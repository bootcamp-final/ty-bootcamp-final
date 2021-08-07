package com.github.buyalsky.shoppingcartservice.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceConstructor
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class ShoppingCart(@Id var customerId: String? = null,
                        var shoppingCartItems: List<ShoppingCartItem> = ArrayList(),
                        @Transient var shipping: Double = 0.0,
                        @Transient var subtotal: Double = 0.0,
                        @Transient var totalAmount: Double = 0.0) {

    @PersistenceConstructor
    constructor(customerId: String?, shoppingCartItems: List<ShoppingCartItem>):
            this(customerId, shoppingCartItems, 0.0, 0.0, 0.0)

    companion object {
        fun newShoppingCartWithId(id: String): ShoppingCart {
            val shoppingCart = ShoppingCart()
            shoppingCart.customerId = id
            return shoppingCart
        }
    }
}
