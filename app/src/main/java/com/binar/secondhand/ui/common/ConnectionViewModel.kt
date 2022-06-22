package com.binar.secondhand.ui.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binar.secondhand.utils.logd

class ConnectionViewModel(context: Context) : ViewModel() {
    private var networkCallback: ConnectivityManager.NetworkCallback
    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _hasConnection = MutableLiveData(false)
    val hasConnection = _hasConnection

    init {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        cm.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onCleared() {
        super.onCleared()
        cm.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            logd("onAvailable: $network")
            val networkCapabilities = cm.getNetworkCapabilities(network)
            if (networkCapabilities == null) _hasConnection.postValue(false)
            val hasInternetCapability = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            logd("onAvailable: ${network}, $hasInternetCapability")
            if (hasInternetCapability == true) {
                _hasConnection.postValue(true)
            }
        }

        override fun onLost(network: Network) {
            logd("onLost: $network")
            _hasConnection.postValue(false)
        }
    }
}