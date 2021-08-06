package com.github.buyalsky.shoppingcartservice.controller

import com.github.buyalsky.shoppingcartservice.entity.ShoppingCart
import com.github.buyalsky.shoppingcartservice.repository.ShoppingCartRepository
import com.github.buyalsky.shoppingcartservice.service.ProductService
import com.github.buyalsky.shoppingcartservice.service.ShoppingCartService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.anyString
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(ShoppingCartController::class)
internal class ShoppingCartControllerTest {

    @MockBean
    private lateinit var shoppingCartService: ShoppingCartService

    @MockBean
    private lateinit var productService: ProductService

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun `should return OK when requesting shopping cart with valid id`() {
        runBlocking {
            val shoppingCart = ShoppingCart()
            shoppingCart.customerId = "1"
            Mockito.`when`(shoppingCartService.findById("1"))
                .thenReturn(shoppingCart)

            webTestClient.get()
                .uri("/{id}", shoppingCart.customerId)
                .exchange()
                .expectStatus().isOk
        }

    }

    @Test
    fun `should return an error when getting the id which is not found`() {
        runBlocking {
            Mockito.`when`(shoppingCartService.findById(anyString()))
                .thenThrow( ShoppingCartNotFoundException("") )

            webTestClient.get()
                .uri("/{id}", "DUMMY_ID")
                .exchange()
                .expectStatus().isNotFound
        }
    }

    @Test
    fun `should create a shopping cart with given user id`() {
        runBlocking {
            val customerId = "anId";
            Mockito.`when`(shoppingCartService.createShoppingCart(customerId))
                .thenReturn(ShoppingCart.newShoppingCartWithId(customerId))

            webTestClient.post()
                .uri("/{id}", customerId)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("\$.customerId").isEqualTo(customerId)
        }
    }
}
