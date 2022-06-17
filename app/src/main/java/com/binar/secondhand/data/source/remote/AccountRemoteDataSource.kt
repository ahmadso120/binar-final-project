package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.AccountService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AccountRemoteDataSource(private val accountService: AccountService) {

    suspend fun getAccount()= accountService.getAccount()

    suspend fun updateAccount(file: MultipartBody.Part?, partMap: Map<String, RequestBody>)= accountService.updateAccount(file,partMap)

}