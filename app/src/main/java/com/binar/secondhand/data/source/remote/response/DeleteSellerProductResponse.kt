package com.binar.secondhand.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class DeleteSellerProductResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String
)