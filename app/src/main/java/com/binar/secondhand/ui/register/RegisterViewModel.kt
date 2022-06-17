package com.binar.secondhand.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.binar.secondhand.data.AuthRepository
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.RegisterRequest
import com.binar.secondhand.data.source.remote.response.RegisterResponse

class RegisterViewModel(
    private val authRepository: AuthRepository
):ViewModel() {
    private val _registerRequest = MutableLiveData<RegisterRequest>()
    val register: LiveData<Result<RegisterResponse>> = _registerRequest.switchMap {
        authRepository.register(it)
    }
    fun doRegisterRequest(registerRequest: RegisterRequest){
        _registerRequest.value =registerRequest
    }


}