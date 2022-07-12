package com.binar.secondhand.ui.common

import androidx.lifecycle.ViewModel
import com.binar.secondhand.data.AuthRepository

class AuthViewModel(repository: AuthRepository) : ViewModel() {

    val isUserHasLoggedIn = repository.isUserHasLoggedIn()
}