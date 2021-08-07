package com.github.buyalsky.shoppingcartservice.service.client

import com.github.buyalsky.shoppingcartservice.dto.ShippingCostDto
import com.github.buyalsky.shoppingcartservice.dto.ShoppingCartDetailDto

interface ShippingCalculationServiceClient {
    suspend fun getShippingCostBasedOnCartDetails(shoppingCartDetailDto: ShoppingCartDetailDto): ShippingCostDto
}
