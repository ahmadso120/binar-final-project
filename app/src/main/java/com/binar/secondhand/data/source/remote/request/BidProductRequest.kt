package com.binar.secondhand.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class BidProductRequest(
    @field:SerializedName("product_id")
    val productId: Int,
    @field:SerializedName("bid_price")
    val bidPrice: Int
)
