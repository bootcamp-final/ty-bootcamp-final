package com.github.buyalsky.shoppingcartservice.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorHandler {
    @ExceptionHandler
    fun exceptionHandler(exception: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(exception.message ?: "Exception Happened", System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [ProductNotFoundException::class, ShoppingCartNotFoundException::class])
    fun exceptionHandler(exception: RuntimeException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(exception.message!!, System.currentTimeMillis()),
                HttpStatus.NOT_FOUND)
    }
}

data class ErrorResponse(val message: String, val timeStamp: Long)

class ShoppingCartNotFoundException(customerId: String)
    : RuntimeException("Shopping cart with the given id ($customerId) cannot found")

class ProductNotFoundException(productId: String)
    : RuntimeException("Product with the given id ($productId) cannot found")
