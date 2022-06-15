package com.binar.secondhand.data.source.remote.request

import okhttp3.MultipartBody
import retrofit2.http.Part


data class AccountRequest(
val full_name: String,
val address: String,
val phone_number: Int,
val email: String,
val password: String,
@Part val image: MultipartBody.Part
)
