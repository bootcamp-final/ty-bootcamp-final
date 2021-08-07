package com.github.buyalsky.shoppingcartservice.service.client

import com.github.buyalsky.shoppingcartservice.dto.ShippingCostDto
import com.github.buyalsky.shoppingcartservice.dto.ShoppingCartDetailDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class ShippingCalculationServiceClientImpl(
    private val webClient: WebClient
) : ShippingCalculationServiceClient {

    @Value("\${shipping-calculation-service.url}")
    private lateinit var shippingCalculationServiceUrl: String

    override suspend fun getShippingCostBasedOnCartDetails
                (shoppingCartDetailDto: ShoppingCartDetailDto): ShippingCostDto =
        webClient.post()
            .uri("${shippingCalculationServiceUrl}/")
            .body(BodyInserters.fromValue(shoppingCartDetailDto))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve().awaitBody()
}
