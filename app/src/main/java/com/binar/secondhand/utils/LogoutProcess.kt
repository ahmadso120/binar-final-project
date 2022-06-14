package com.binar.secondhand.utils

import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.binar.secondhand.storage.AppLocalData

object LogoutProcess {

    fun execute(appLocalData: AppLocalData, binding: ViewBinding) {
        appLocalData.dropUserLoggedIn()
        goToLoginFragment(binding)
    }

    private fun goToLoginFragment(binding: ViewBinding) {
        val navController = binding.root.findNavController()
        val startDestination = navController.graph.startDestinationId
        val currentDestination = navController.currentDestination?.id
        val navOptions = currentDestination?.let {
            NavOptions.Builder()
                .setPopUpTo(it, true)
                .build()
        }
        navController.navigate(startDestination, null, navOptions)
    }
}