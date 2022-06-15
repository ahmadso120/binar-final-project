package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.data.source.remote.request.AccountRequest
import com.binar.secondhand.data.source.remote.response.AccountResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface AccountService {

    @GET("auth/user/1")
    suspend fun getAccount(): Response<AccountResponse>

    @PUT("auth/user/1")
    suspend fun updateAccount(@Body accountRequest: AccountRequest): Response<AccountResponse>

}