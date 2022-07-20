package com.binar.secondhand.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class WishlistResponse(
    val id: Int,
    @field:SerializedName("product_id")
    val productId: Int,
    @field:SerializedName("user_id")
    val userId: Int,
    @field:SerializedName("Product")
    val product: BuyerProductResponse
)