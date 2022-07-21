package com.binar.secondhand.data.source.remote.network


import com.binar.secondhand.data.source.remote.request.AccountSettingRequest
import com.binar.secondhand.data.source.remote.response.AccountSettingResponse
import com.binar.secondhand.data.source.remote.response.ChangePasswordResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface AccountSettingService {
    @PUT("auth/change-password")
    suspend fun changePassEmail(@Body bodyAccount : AccountSettingRequest): Response<ChangePasswordResponse>
}