package com.github.buyalsky.shoppingcartservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
class ShoppingCartServiceApplication {
    @Bean
    fun webClient(): WebClient {
        return WebClient.create()
    }
}

fun main(args: Array<String>) {
    runApplication<ShoppingCartServiceApplication>(*args)
}
