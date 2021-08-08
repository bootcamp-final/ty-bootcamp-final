package com.github.buyalsky.shoppingcartservice.service

import com.github.buyalsky.shoppingcartservice.controller.Listener
import com.github.buyalsky.shoppingcartservice.dto.*
import com.github.buyalsky.shoppingcartservice.entity.ShoppingCart
import com.github.buyalsky.shoppingcartservice.entity.ShoppingCartItem
import com.github.buyalsky.shoppingcartservice.repository.ShoppingCartRepository
import com.github.buyalsky.shoppingcartservice.service.client.ProductServiceClient
import com.github.buyalsky.shoppingcartservice.service.client.ShippingCalculationServiceClient
import com.github.buyalsky.shoppingcartservice.service.client.UserServiceClient
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.producer.Producer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext

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
    private lateinit var userServiceClient: UserServiceClient
    private lateinit var shippingCalculationServiceClient: ShippingCalculationServiceClient
    private lateinit var productServiceClient: ProductServiceClient

    private lateinit var shoppingCartService: ShoppingCartService

    @BeforeAll
    fun setup(kafkaBroker: EmbeddedKafkaBroker) {
        repository = Mockito.mock(ShoppingCartRepository::class.java)
        kafkaListener = Mockito.mock(Listener::class.java)
        productService = Mockito.mock(ProductService::class.java)
        userServiceClient = Mockito.mock(UserServiceClient::class.java)
        shippingCalculationServiceClient = Mockito.mock(ShippingCalculationServiceClient::class.java)
        productServiceClient = Mockito.mock(ProductServiceClient::class.java)
        shoppingCartService = ShoppingCartServiceImpl(repository,
            productService, userServiceClient, shippingCalculationServiceClient, productServiceClient)
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
    fun findByIdCompleteInfo() {
        runBlocking {
            val customerId = "An_id"
            val productId = "an id"

            val productInfoDto = ProductInfoDto(productId, "a product", "a category", 5.5, 10)
            val shoppingCartItem = ShoppingCartItem(productId, 5, 5.5)


            Mockito.`when`(repository.findById(customerId))
                .thenReturn(ShoppingCart(customerId, listOf(shoppingCartItem)))

            Mockito.`when`(productServiceClient.getProductInfoById(productId))
                .thenReturn(productInfoDto)


            Mockito.`when`(shippingCalculationServiceClient
                .getShippingCostBasedOnCartDetails(anyObject()))
                .thenReturn(ShippingCostDto(3.4))

            Mockito.`when`(userServiceClient.getUserDetailById(customerId))
                .thenReturn(UserDetailDto(customerId, "a name", "an email", true))

            val shoppingCartExpected = ShoppingCart(customerId, listOf(shoppingCartItem), 3.4, 5 * 5.5, 5 * 5.5 + 3.4)

            val shoppingCartFound = shoppingCartService.findByIdCompleteInfo(customerId)
            assertEquals(shoppingCartExpected, shoppingCartFound)
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

    private fun <T> anyObject(): T {
        return Mockito.anyObject<T>()
    }


}
