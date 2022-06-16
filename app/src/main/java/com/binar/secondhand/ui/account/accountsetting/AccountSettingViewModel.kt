package com.binar.secondhand.ui.account.accountsetting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.binar.secondhand.data.AccSettRepo
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.response.AccountSettingResponse


class AccountSettingViewModel(
    private val acc: AccSettRepo
) : ViewModel(){


    fun getUser(): LiveData<Result<AccountSettingResponse>> {
        return acc.getUser()
    }


}