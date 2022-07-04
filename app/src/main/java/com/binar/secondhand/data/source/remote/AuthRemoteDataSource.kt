package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.AuthService
import com.binar.secondhand.data.source.remote.request.LoginRequest
import com.binar.secondhand.data.source.remote.request.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuthRemoteDataSource(
    private val authService: AuthService
) {
    suspend fun login(loginRequest: LoginRequest) =
        authService.login(loginRequest)
//    suspend fun register(registerRequest: RegisterRequest)=
//        authService.register(registerRequest)
suspend fun register(file: MultipartBody.Part?, partMap: Map<String, RequestBody>)=
    authService.register(file,partMap)
}


