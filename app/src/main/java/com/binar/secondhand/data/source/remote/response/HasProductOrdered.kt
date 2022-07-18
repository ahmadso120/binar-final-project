package com.binar.secondhand.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class HasProductOrdered(
    @field:SerializedName("product_id")
    val productId: Int
)
