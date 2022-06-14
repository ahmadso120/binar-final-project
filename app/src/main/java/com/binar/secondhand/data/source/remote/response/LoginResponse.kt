package com.binar.secondhand.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val name: String,
    val email: String,

    @field:SerializedName("access_token")
    val accessToken: String
)
