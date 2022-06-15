package com.binar.secondhand.data.source.remote

import com.binar.secondhand.data.source.remote.network.AccountService

class AccountRemoteDataSource(private val accountService: AccountService) {

    suspend fun getAccount()= accountService.getAccount()

}