package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.data.source.remote.response.AccountResponse
import retrofit2.Response
import retrofit2.http.GET

interface AccountService {

    @GET("auth/user/1")
    suspend fun getAccount(): Response<AccountResponse>

}