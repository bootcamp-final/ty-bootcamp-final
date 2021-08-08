package com.github.buyalsky.shoppingcartservice.controller

import com.github.buyalsky.shoppingcartservice.dto.*
import com.github.buyalsky.shoppingcartservice.service.ProductService
import com.github.buyalsky.shoppingcartservice.service.client.EmailServiceClient
import com.github.buyalsky.shoppingcartservice.service.client.UserServiceClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import reactor.core.publisher.Mono

@Component
class KafkaListener(
    private val productService: ProductService,
    private val userServiceClient: UserServiceClient,
    private val emailServiceClient: EmailServiceClient
): Listener {

    @KafkaListener(topics = ["\${topic.price-change.name}"],
        groupId = "\${group.id}",
        containerFactory = "listenerContainerForPriceChange")
    override fun listenProductChangeEvent(priceChangeDto: ProductPriceChangeDto) {
        val productId = priceChangeDto.productId!!
        val productName = priceChangeDto.productName!!
        val previousPrice = priceChangeDto.previousPrice!!
        val currentPrice = priceChangeDto.newPrice!!

        runBlocking {
            val cartsThatFollowProduct = productService
                .getCartsThatFollowProduct(productId)

            cartsThatFollowProduct.forEach { userId ->
                val (_, fullName, emailAddress) = userServiceClient.getUserDetailById(userId)

                val productPriceChangeEmailDto = ProductPriceChangeEmailDto(
                    productName,
                    fullName,
                    emailAddress,
                    previousPrice,
                    currentPrice
                )
                emailServiceClient.sendProductPriceChangeEvent(productPriceChangeEmailDto)
            }
        }
    }

    @KafkaListener(topics = ["\${topic.stock-change.name}"],
        groupId = "\${group.id}",
        containerFactory = "listenerContainerForStockChange")
    override fun listenStockChangeEvent(stockChangeDto: ProductStockChangeDto) {
        runBlocking {
            println(stockChangeDto)
        }
    }
}
