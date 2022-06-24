package com.binar.secondhand.utils

import androidx.navigation.NavController
import com.binar.secondhand.storage.AppLocalData

object LogoutProcess {
    fun execute(appLocalData: AppLocalData, navController: NavController) {
        appLocalData.dropUserLoggedIn()
        navController.navigateToStartDestination()
    }
}