package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.data.source.remote.request.LoginRequest
import com.binar.secondhand.data.source.remote.request.RegisterRequest
import com.binar.secondhand.data.source.remote.response.LoginResponse
import com.binar.secondhand.data.source.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AuthService {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @Multipart
    @JvmSuppressWildcards
    @POST("auth/register")
    suspend fun register(@Part file: MultipartBody.Part?,
                         @PartMap partMap: Map<String, RequestBody>): Response<RegisterResponse>
}