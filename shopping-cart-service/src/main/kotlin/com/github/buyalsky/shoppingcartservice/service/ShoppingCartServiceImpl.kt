package com.github.buyalsky.shoppingcartservice.service

import com.github.buyalsky.shoppingcartservice.controller.ShoppingCartNotFoundException
import com.github.buyalsky.shoppingcartservice.dto.ShoppingCartDetailDto
import com.github.buyalsky.shoppingcartservice.entity.ShoppingCart
import com.github.buyalsky.shoppingcartservice.entity.ShoppingCartItem
import com.github.buyalsky.shoppingcartservice.repository.ShoppingCartRepository
import com.github.buyalsky.shoppingcartservice.service.client.ProductServiceClient
import com.github.buyalsky.shoppingcartservice.service.client.ShippingCalculationServiceClient
import com.github.buyalsky.shoppingcartservice.service.client.UserServiceClient
import org.springframework.stereotype.Service

@Service
class ShoppingCartServiceImpl(
    private val repository: ShoppingCartRepository,
    private val productService: ProductService,
    private val userServiceClient: UserServiceClient,
    private val shippingCalculationServiceClient: ShippingCalculationServiceClient,
    private val productServiceClient: ProductServiceClient
): ShoppingCartService {

    override suspend fun findById(id: String) =
        repository.findById(id)?: throw ShoppingCartNotFoundException(id)

    override suspend fun findByIdCompleteInfo(id: String): ShoppingCart {
        val (customerId, shoppingCartItems, _) = repository.findById(id)?: throw ShoppingCartNotFoundException(id)
        var subtotal = 0.0
        val categories: MutableSet<String> = HashSet()
        val newShoppingCartItems: MutableList<ShoppingCartItem> = ArrayList()
        shoppingCartItems.forEach {
            val productInfo = productServiceClient.getProductInfoById(it.productId)

            newShoppingCartItems.add(ShoppingCartItem(it.productId, it.quantity, productInfo.price))
            categories.add(productInfo.category)
        }

        newShoppingCartItems.forEach {
            subtotal += it.price*it.quantity
        }
        val shipping = getShippingCostForGivenCart(customerId!!, subtotal, categories.toList())
        return ShoppingCart(customerId, newShoppingCartItems, shipping, subtotal, shipping+subtotal)
    }

    override suspend fun createShoppingCart(customerId: String): ShoppingCart {
        val shoppingCart = ShoppingCart.newShoppingCartWithId(customerId)
        return repository.save(shoppingCart)
    }

    override suspend fun deleteShoppingCart(customerId: String) =
        repository.deleteById(customerId)

    override suspend fun addProductToShoppingCart(customerId: String, productId: String, quantity: Int): ShoppingCart {
        val shoppingCart = findById(customerId)
        val shoppingCartItemForGivenProduct = shoppingCart.shoppingCartItems.find { i -> i.productId == productId }
        val newShoppingCartItems = shoppingCart.shoppingCartItems.filter { i -> i.productId != productId}.toMutableList()

        if (shoppingCartItemForGivenProduct == null) {
            newShoppingCartItems += ShoppingCartItem(productId, quantity)
            shoppingCart.shoppingCartItems = newShoppingCartItems
            productService.addFollowerToProduct(productId, customerId)
        } else {
            shoppingCartItemForGivenProduct.quantity += quantity
            newShoppingCartItems += shoppingCartItemForGivenProduct
            shoppingCart.shoppingCartItems = newShoppingCartItems
        }

        return repository.save(shoppingCart)
    }

    override suspend fun subtractItemFromCart(customerId: String, productId: String, quantity: Int): ShoppingCart {
        val shoppingCart = findById(customerId)
        val shoppingCartItem = shoppingCart.shoppingCartItems.find { item -> item.productId == productId }
        val newShoppingCartItems = shoppingCart.shoppingCartItems
            .filter { i -> i.productId != productId }.toMutableList()

        if (shoppingCartItem == null)
            return shoppingCart

        shoppingCartItem.quantity -= quantity
        if (shoppingCartItem.quantity <= 0) {
            shoppingCart.shoppingCartItems = shoppingCart.shoppingCartItems.filter { i -> i.productId != productId }
            return shoppingCart
        }

        newShoppingCartItems += shoppingCartItem
        shoppingCart.shoppingCartItems = newShoppingCartItems
        return repository.save(shoppingCart)
    }

    override suspend fun removeItemFromCart(customerId: String, productId: String): ShoppingCart {
        val shoppingCart = findById(customerId)
        shoppingCart.shoppingCartItems.filter { item -> item.productId != productId }
        return repository.save(shoppingCart)
    }

    internal suspend fun getShippingCostForGivenCart(customerId: String, subtotal: Double, categories: List<String>): Double {
        val (_, _, _, isEliteMember) = userServiceClient.getUserDetailById(customerId)

        val shippingCostDto = shippingCalculationServiceClient
            .getShippingCostBasedOnCartDetails(ShoppingCartDetailDto(isEliteMember, subtotal, categories))

        return shippingCostDto.shippingCost
    }

}
