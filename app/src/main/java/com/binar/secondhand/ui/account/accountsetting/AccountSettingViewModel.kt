package com.binar.secondhand.ui.account.accountsetting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.binar.secondhand.data.AccSettRepo
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.AccountReq
import com.binar.secondhand.data.source.remote.response.AccountSettingResponse


class AccountSettingViewModel(
    private val acc: AccSettRepo
) : ViewModel(){

    private val _putUser = MutableLiveData<AccountReq>()
    val userResp : LiveData<Result<AccountSettingResponse>> = _putUser.switchMap {
        acc.putUser(it)
    }


    fun getUser(): LiveData<Result<AccountSettingResponse>> {
        return acc.getUser()
    }

    fun getDataChange(req : AccountReq){
        _putUser.value = req
    }


}