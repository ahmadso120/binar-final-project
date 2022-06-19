package com.binar.secondhand.data.source.remote.network


import com.binar.secondhand.data.source.remote.response.AccountResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AccountService {

    @GET("auth/user/1")
    suspend fun getAccount(): Response<AccountResponse>

    @Multipart
    @JvmSuppressWildcards
    @PUT("auth/user/1")
    suspend fun updateAccount(
        @Part file: MultipartBody.Part?,
        @PartMap partMap: Map<String, RequestBody>,
        ): Response<AccountResponse>

}