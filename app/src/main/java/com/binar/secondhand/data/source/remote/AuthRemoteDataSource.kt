package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.ApiService
import com.binar.secondhand.data.source.remote.request.LoginRequest

class AuthRemoteDataSource(
    private val apiService: ApiService
) {
    suspend fun login(loginRequest: LoginRequest) =
        apiService.login(loginRequest)
}