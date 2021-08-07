package com.github.buyalsky.shoppingcartservice.service.client

import com.github.buyalsky.shoppingcartservice.dto.ProductPriceChangeEmailDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class EmailServiceClientImpl(
    private val webClient: WebClient
) : EmailServiceClient {

    @Value("\${email-service.url}")
    private lateinit var emailServiceUrl: String

    override suspend fun sendProductPriceChangeEvent
                (productPriceChangeEmailDto: ProductPriceChangeEmailDto): Any =
        webClient.post()
            .uri("${emailServiceUrl}/")
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(productPriceChangeEmailDto))
            .retrieve()
            .awaitBody()

}
