package com.binar.secondhand.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @field:SerializedName("base_price")
    val basePrice: Int,
    val description: String,
    @field:SerializedName("image_name")
    val imageName: String,
    @field:SerializedName("image_url")
    val imageUrl: String,
    val location: String,
    val name: String,
    val status: String,
    @field:SerializedName("user_id")
    val userId: Int
)