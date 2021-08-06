package com.github.buyalsky.shoppingcartservice.dto

import com.github.buyalsky.shoppingcartservice.dto.ProductStockChangeDto
import java.util.*

class ProductStockChangeDto {
    var productId: String? = null
    var currentLeftInStock: Int? = null

    constructor() {}
    constructor(productId: String?, currentLeftInStock: Int?) {
        this.productId = productId
        this.currentLeftInStock = currentLeftInStock
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as ProductStockChangeDto
        return productId == that.productId && currentLeftInStock == that.currentLeftInStock
    }

    override fun hashCode(): Int {
        return Objects.hash(productId, currentLeftInStock)
    }
}
