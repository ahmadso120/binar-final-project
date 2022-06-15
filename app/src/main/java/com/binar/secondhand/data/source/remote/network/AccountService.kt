package com.binar.secondhand.data.source.remote.network

import retrofit2.http.GET

interface AccountService {

    @GET("auth/user/1")
    suspend fun getAccount()

}