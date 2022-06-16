package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.AuthService
import com.binar.secondhand.data.source.remote.request.LoginRequest
import com.binar.secondhand.data.source.remote.request.RegisterRequest

class AuthRemoteDataSource(
    private val authService: AuthService
) {
    suspend fun login(loginRequest: LoginRequest) =
        authService.login(loginRequest)
    suspend fun register(registerRequest: RegisterRequest)=
        authService.register(registerRequest)
}