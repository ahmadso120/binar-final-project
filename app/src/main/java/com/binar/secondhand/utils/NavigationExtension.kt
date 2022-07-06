package com.binar.secondhand.utils

import androidx.navigation.NavController
import androidx.navigation.NavOptions

fun NavController.navigateToStartDestination() {
    val startDestination = this.graph.startDestinationId
    val navOptions = NavOptions.Builder()
        .setPopUpTo(startDestination, true)
        .build()
    this.navigate(startDestination, null, navOptions)
}