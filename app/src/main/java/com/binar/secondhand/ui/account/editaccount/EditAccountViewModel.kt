package com.binar.secondhand.ui.account.editaccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.binar.secondhand.data.AccountRepository
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.AccountRequest
import com.binar.secondhand.data.source.remote.response.AccountResponse

class EditAccountViewModel(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _updateAccountRequest = MutableLiveData<AccountRequest>()
    val updateAccount: LiveData<Result<AccountResponse>> = _updateAccountRequest.switchMap {
        accountRepository.updateAccount(it.file, it.partMap)
    }

    fun doUpdateAccountRequest(accountRequest: AccountRequest){
        _updateAccountRequest.value=accountRequest
    }


     fun getAccount(): LiveData<Result<AccountResponse>> {
        return accountRepository.getAccount()
    }
}