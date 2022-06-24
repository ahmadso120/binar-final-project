package com.binar.secondhand.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class SellerOrderResponse(
    @field:SerializedName("Product")
    val product: ProductResponse,
    @field:SerializedName("User")
    val userResponse: UserResponse,
    @field:SerializedName("buyer_id")
    val buyerId: Int,
    val createdAt: String,
    val id: Int,
    val price: Int,
    @field:SerializedName("product_id")
    val productId: Int,
    val status: String,
    val updatedAt: String
)