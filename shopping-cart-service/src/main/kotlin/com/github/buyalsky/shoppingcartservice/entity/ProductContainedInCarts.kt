package com.github.buyalsky.shoppingcartservice.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class ProductContainedInCarts(
        @Id var productId: String? = null,
        var cartIdsThatContainProduct: Set<String> = HashSet()
)
