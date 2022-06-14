package com.binar.secondhand.storage

data class UserLoggedIn(
    val accessToken: String,
    val name: String,
    val email: String
)