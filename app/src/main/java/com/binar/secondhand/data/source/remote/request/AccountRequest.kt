package com.binar.secondhand.data.source.remote.request

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part



data class AccountRequest(
@Part val file: MultipartBody.Part,
@Part val partMap: Map<String, RequestBody>
)
