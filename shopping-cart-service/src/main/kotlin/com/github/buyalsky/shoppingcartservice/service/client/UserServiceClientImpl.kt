package com.github.buyalsky.shoppingcartservice.service.client

import com.github.buyalsky.shoppingcartservice.dto.UserDetailDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class UserServiceClientImpl(
    private val webClient: WebClient
): UserServiceClient {
    @Value("\${user-service.url}")
    private lateinit var userServiceUrl: String

    override suspend fun getUserDetailById(id: String): UserDetailDto = webClient.get()
        .uri("${userServiceUrl}/{customerId}", id)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve().awaitBody()
}
