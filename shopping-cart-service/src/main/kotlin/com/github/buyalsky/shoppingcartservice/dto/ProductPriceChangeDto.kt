package com.github.buyalsky.shoppingcartservice.dto

import com.github.buyalsky.shoppingcartservice.dto.ProductPriceChangeDto
import java.util.*

class ProductPriceChangeDto {
    var productId: String? = null
    var previousPrice: Double? = null
    var newPrice: Double? = null
    var productName: String? = null

    constructor()

    constructor(productId: String?, productName: String?, previousPrice: Double?, newPrice: Double?) {
        this.productId = productId
        this.productName = productName
        this.previousPrice = previousPrice
        this.newPrice = newPrice
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as ProductPriceChangeDto
        return productId == that.productId && previousPrice == that.previousPrice && newPrice == that.newPrice
                && productName == that.productName
    }

    override fun hashCode(): Int {
        return Objects.hash(productId, productName, previousPrice, newPrice)
    }
}
