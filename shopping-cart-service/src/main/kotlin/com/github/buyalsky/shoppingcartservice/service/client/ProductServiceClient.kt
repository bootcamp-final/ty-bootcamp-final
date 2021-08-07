package com.github.buyalsky.shoppingcartservice.service.client

import com.github.buyalsky.shoppingcartservice.dto.ProductInfoDto

interface ProductServiceClient {
    suspend fun getProductInfoById(id: String): ProductInfoDto
}
