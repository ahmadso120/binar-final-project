package com.binar.secondhand.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.binar.secondhand.data.AuthRepository
import com.binar.secondhand.data.Result
import com.binar.secondhand.data.source.remote.request.LoginRequest
import com.binar.secondhand.data.source.remote.response.LoginResponse
import com.binar.secondhand.storage.UserLoggedIn

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginRequest = MutableLiveData<LoginRequest>()
    val login: LiveData<Result<LoginResponse>> = _loginRequest.switchMap {
        authRepository.login(it)
    }

    fun doLoginRequest(loginRequest: LoginRequest) {
        _loginRequest.value = loginRequest
    }

    fun setUserLoggedIn(userLoggedIn: UserLoggedIn) = authRepository.setUserLoggedIn(userLoggedIn)
}