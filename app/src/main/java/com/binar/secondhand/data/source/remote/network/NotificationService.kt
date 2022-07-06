package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.data.source.remote.response.NotificationResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface NotificationService {

    @GET("notification")
    suspend fun getAllNotification(): Response<List<NotificationResponseItem>>

    @PATCH("notification/{id}")
    suspend fun patchNotification(
        @Path("id") id : Int
    ): Response<NotificationResponseItem>
}