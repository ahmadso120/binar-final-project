package com.binar.secondhand.data.source.remote.request

data class RegisterRequest(
    val full_name: String,
    val email: String,
    val password: String,
    val phone_number: String,
    val address :String
)
