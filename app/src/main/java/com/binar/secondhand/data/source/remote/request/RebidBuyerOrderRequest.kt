package com.binar.secondhand.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class RebidBuyerOrderRequest(
    @SerializedName("bid_price")
    val bidPrice: Int
)
