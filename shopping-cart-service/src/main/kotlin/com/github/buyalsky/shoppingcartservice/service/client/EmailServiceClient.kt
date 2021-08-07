package com.github.buyalsky.shoppingcartservice.service.client

import com.github.buyalsky.shoppingcartservice.dto.ProductPriceChangeEmailDto

interface EmailServiceClient {
    suspend fun sendProductPriceChangeEvent(productPriceChangeEmailDto: ProductPriceChangeEmailDto): Any
}
