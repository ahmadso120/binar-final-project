package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.data.source.remote.request.LoginRequest
import com.binar.secondhand.data.source.remote.request.RegisterRequest
import com.binar.secondhand.data.source.remote.response.BuyerProductResponse
import com.binar.secondhand.data.source.remote.response.LoginResponse
import com.binar.secondhand.data.source.remote.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.*

interface AuthService {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>
}