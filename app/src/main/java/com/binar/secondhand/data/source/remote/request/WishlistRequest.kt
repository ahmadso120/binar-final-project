package com.binar.secondhand.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class WishlistRequest(
    @field:SerializedName("product_id")
    val productId: Int
)
