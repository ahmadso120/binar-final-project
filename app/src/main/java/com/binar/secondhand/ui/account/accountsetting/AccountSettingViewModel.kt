package com.binar.secondhand.ui.account.accountsetting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.binar.secondhand.data.AccSettRepo
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.AccountSettingRequest
import com.binar.secondhand.data.source.remote.response.AccountSettingResponse
import com.binar.secondhand.data.source.remote.response.ChangePasswordResponse


class AccountSettingViewModel(
    private val acc: AccSettRepo
) : ViewModel(){

    private val _putUser = MutableLiveData<AccountSettingRequest>()
    val userResp : LiveData<Result<ChangePasswordResponse>> = _putUser.switchMap {
        acc.putUser(it)
    }

    fun getDataChange(req : AccountSettingRequest){
        _putUser.value = req
    }

}