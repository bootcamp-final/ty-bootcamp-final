package com.github.buyalsky.shoppingcartservice.controller

import com.github.buyalsky.shoppingcartservice.dto.ProductPriceChangeDto
import com.github.buyalsky.shoppingcartservice.dto.ProductStockChangeDto

interface Listener {

    fun listenProductChangeEvent(priceChangeDto: ProductPriceChangeDto)

    fun listenStockChangeEvent(stockChangeDto: ProductStockChangeDto)
}
