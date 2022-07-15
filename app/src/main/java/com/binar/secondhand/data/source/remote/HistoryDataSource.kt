package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.AuthService
import com.binar.secondhand.data.source.remote.network.HistoryService
import com.binar.secondhand.data.source.remote.request.LoginRequest
import com.binar.secondhand.data.source.remote.request.RegisterRequest

class HistoryDataSource(
    private val authService: HistoryService
) {
    suspend fun history() = authService.history()
}


