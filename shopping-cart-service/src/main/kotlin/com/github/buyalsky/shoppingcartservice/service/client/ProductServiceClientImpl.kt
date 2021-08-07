package com.github.buyalsky.shoppingcartservice.service.client

import com.github.buyalsky.shoppingcartservice.dto.ProductInfoDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class ProductServiceClientImpl(
    private val webClient: WebClient
) : ProductServiceClient {

    @Value("\${product-service.url}")
    private lateinit var productServiceUrl: String

    override suspend fun getProductInfoById(id: String): ProductInfoDto = webClient.get()
        .uri("${productServiceUrl}/{productId}", id)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .awaitBody<ProductInfoDto>()
}
