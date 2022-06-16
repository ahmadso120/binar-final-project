package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.AccountSettingService


class AccSettDataSource(
    private val acc : AccountSettingService
) {
    suspend fun getUser()= acc.getUser()
}