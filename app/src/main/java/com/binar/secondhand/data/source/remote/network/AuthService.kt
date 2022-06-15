package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.data.source.remote.request.LoginRequest
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.data.source.remote.response.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface AuthService {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}