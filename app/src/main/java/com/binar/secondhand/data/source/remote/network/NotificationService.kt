package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.data.source.remote.response.NotificationResponseItem
import retrofit2.Response
import retrofit2.http.GET

interface NotificationService {

    @GET("notification")
    suspend fun getAllNotification(): Response<List<NotificationResponseItem>>
}