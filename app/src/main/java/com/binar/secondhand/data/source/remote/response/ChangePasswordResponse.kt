package com.binar.secondhand.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String
)