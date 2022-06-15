package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.AuthService
import com.binar.secondhand.data.source.remote.request.LoginRequest

class AuthRemoteDataSource(
    private val authService: AuthService
) {
    suspend fun login(loginRequest: LoginRequest) =
        authService.login(loginRequest)
}