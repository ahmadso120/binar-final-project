package com.binar.secondhand.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val address: String,
    val city: String,
    val email: String,
    @field:SerializedName("full_name")
    val fullName: String,
    @field:SerializedName("phone_number")
    val phoneNumber: String
)