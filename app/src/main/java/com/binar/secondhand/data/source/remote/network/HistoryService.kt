package com.binar.secondhand.data.source.remote.network

import com.binar.secondhand.data.source.remote.response.HistoryResponseItem
import retrofit2.Response
import retrofit2.http.GET

interface HistoryService {
    @GET("/history")
    suspend fun history(): Response<List<HistoryResponseItem>>
}