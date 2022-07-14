package com.binar.secondhand.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.binar.secondhand.R
import com.binar.secondhand.ui.common.AuthViewModel
import com.binar.secondhand.ui.login.LoginFragment.Companion.LOGIN_SUCCESSFUL

object RequireAuthentication {

    fun execute(
        authViewModel: AuthViewModel,
        navController: NavController,
        lifecycleOwner: LifecycleOwner
    ) {
        val savedStateHandle = navController.currentBackStackEntry!!.savedStateHandle
        savedStateHandle.getLiveData<Boolean>(LOGIN_SUCCESSFUL).observe(lifecycleOwner) {
            if (!it) {
                navController.navigateToStartDestination()
            }
        }

        lifecycleOwner.lifecycleScope.launchWhenCreated {
            authViewModel.isUserHasLoggedIn.collect {
                if (!it) {
                    navController.navigate(R.id.loginFragment)
                }
            }
        }
    }
}