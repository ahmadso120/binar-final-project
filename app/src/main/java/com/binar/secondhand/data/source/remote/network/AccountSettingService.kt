package com.binar.secondhand.data.source.remote.network


import com.binar.secondhand.data.source.remote.request.AccountSettingRequest
import com.binar.secondhand.data.source.remote.response.AccountSettingResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface AccountSettingService {
    @GET("auth/user")
    suspend fun getUser(): Response<AccountSettingResponse>

    @PUT("auth/user")
    suspend fun changePassEmail(@Body bodyAccount : AccountSettingRequest): Response<AccountSettingResponse>
}