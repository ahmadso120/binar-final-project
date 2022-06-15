package com.binar.secondhand.ui.account.editaccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.binar.secondhand.data.AccountRepository
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.response.AccountResponse

class EditAccountViewModel(
    private val accountRepository: AccountRepository
) : ViewModel() {


     fun getAccount(): LiveData<Result<AccountResponse>> {
        return accountRepository.getAccount()
    }


}