package com.binar.secondhand.data.source.remote.request

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.PartMap

data class RegisterRequest(
    val full_name: String,
    val email: String,
    val password: String,
)
