package com.binar.secondhand.data.source.remote.request

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.PartMap

data class SellerProductRequest (
    @Part val file: MultipartBody.Part?,
    @PartMap val partMap: Map<String, RequestBody>
)