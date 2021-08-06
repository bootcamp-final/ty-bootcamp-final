package com.github.buyalsky.shoppingcartservice.service

import com.github.buyalsky.shoppingcartservice.controller.Listener
import com.github.buyalsky.shoppingcartservice.dto.ProductPriceChangeDto
import com.github.buyalsky.shoppingcartservice.dto.ProductStockChangeDto
import com.github.buyalsky.shoppingcartservice.entity.ShoppingCart
import com.github.buyalsky.shoppingcartservice.repository.ShoppingCartRepository
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.producer.Producer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.web.reactive.function.client.WebClient

@DirtiesContext
@EmbeddedKafka(topics = ["test-price-change", "test-stock-change"])
@ExtendWith(MockitoExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ShoppingCartServiceImplTest {

    private val TOPIC_NAME_PRICE_CHANGE = "test-price-change"
    private val TOPIC_NAME_STOCK_CHANGE = "test-stock-change"

    private lateinit var kafkaTemplatePriceChange: Producer<String, ProductPriceChangeDto>
    private lateinit var kafkaTemplateStockChange: Producer<String, ProductStockChangeDto>

    private lateinit var kafkaListener: Listener

    private lateinit var repository: ShoppingCartRepository

    private lateinit var productService: ProductService

    private lateinit var shoppingCartService: ShoppingCartService

    @BeforeAll
    fun setup(kafkaBroker: EmbeddedKafkaBroker) {
        repository = Mockito.mock(ShoppingCartRepository::class.java)
        kafkaListener = Mockito.mock(Listener::class.java)
        val webClientMock = Mockito.mock(WebClient::class.java)
        productService = Mockito.mock(ProductService::class.java)
        shoppingCartService = ShoppingCartServiceImpl(repository, productService, webClientMock)
    }

    @Test
    fun findById() {
        runBlocking {
            //given
            val customerId = "AN_ID"
            val savedCart = ShoppingCart.newShoppingCartWithId(customerId)
            Mockito.`when`(repository.findById(customerId))
                .thenReturn(savedCart)

            //when
            val foundCart = shoppingCartService.findById(customerId)

            //then
            assertEquals(savedCart, foundCart)
        }
    }

    @Test
    fun createShoppingCart() {
        runBlocking {
            //given
            val customerId = "AN_ID"
            val savedCart = ShoppingCart.newShoppingCartWithId(customerId)
            Mockito.`when`(repository.save(savedCart))
                .thenReturn(savedCart)

            //when
            val createdCart = shoppingCartService.createShoppingCart(customerId)
            assertEquals(savedCart, createdCart)
        }
    }

    @Test
    fun deleteShoppingCart() {
        runBlocking {
            //given
            val savedCartsStub = mutableSetOf<ShoppingCart>()
            val customerId = "An_id"
            val shoppingCart = ShoppingCart.newShoppingCartWithId(customerId)

            Mockito.`when`(repository.findById(customerId))
                .thenReturn(
                    if (savedCartsStub.contains(shoppingCart)) shoppingCart
                    else null
                )

            Mockito.doAnswer {
                savedCartsStub.add(shoppingCart)
            }.`when`(repository).save(shoppingCart)

            Mockito.doAnswer {
                savedCartsStub.remove(shoppingCart)
            }.`when`(repository).delete(shoppingCart)

            repository.save(shoppingCart)

            //when
            shoppingCartService.deleteShoppingCart(customerId)

            //then
            assertNull(repository.findById(customerId))
        }
    }

    @Test
    fun addProductToShoppingCart() {
    }

    @Test
    fun subtractItemFromCart() {
    }

    @Test
    fun removeItemFromCart() {
    }
}
