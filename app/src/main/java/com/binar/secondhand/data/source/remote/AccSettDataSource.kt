package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.AccountSettingService
import com.binar.secondhand.data.source.remote.request.AccountReq


class AccSettDataSource(
    private val acc : AccountSettingService
) {
    suspend fun getUser()= acc.getUser()

    suspend fun putUser(body : AccountReq)=acc.changePassEmail(body)
}