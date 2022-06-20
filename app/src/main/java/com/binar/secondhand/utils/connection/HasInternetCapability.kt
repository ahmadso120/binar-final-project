package com.binar.secondhand.utils.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class HasInternetCapability (val context: Context) {
     val isConnected: Boolean
     get() {
         val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                 as ConnectivityManager
         val nw      = cm.activeNetwork ?: return false
         val networkCapabilities = cm.getNetworkCapabilities(nw) ?: return false
         return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
     }
}