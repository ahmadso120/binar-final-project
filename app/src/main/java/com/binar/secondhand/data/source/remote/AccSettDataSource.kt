package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.AccountSettingService
import com.binar.secondhand.data.source.remote.request.AccountSettingRequest


class AccSettDataSource(
    private val acc : AccountSettingService
) {
    suspend fun putUser(body : AccountSettingRequest)=acc.changePassEmail(body)
}