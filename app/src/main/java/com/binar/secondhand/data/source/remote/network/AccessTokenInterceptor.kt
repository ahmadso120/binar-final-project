package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.storage.AppLocalData
import com.binar.secondhand.utils.loge
import okhttp3.Interceptor
import okhttp3.Response

class AccessTokenInterceptor (
    private val appLocalData: AppLocalData
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = appLocalData.getAccessToken

        return if (!token.isNullOrEmpty()) {
            val authenticatedRequestBody = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("access_token", token)
                .build()
            chain.proceed(authenticatedRequestBody)
        } else {
            val authenticatedRequestBody = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(authenticatedRequestBody)
        }
    }
}