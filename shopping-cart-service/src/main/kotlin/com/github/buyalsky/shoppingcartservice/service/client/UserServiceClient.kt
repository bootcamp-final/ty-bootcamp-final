package com.github.buyalsky.shoppingcartservice.service.client

import com.github.buyalsky.shoppingcartservice.dto.UserDetailDto

interface UserServiceClient {
    suspend fun getUserDetailById(id: String): UserDetailDto
}
