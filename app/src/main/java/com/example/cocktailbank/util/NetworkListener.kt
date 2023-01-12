package com.example.cocktailbank.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import androidx.core.content.getSystemService
import kotlinx.coroutines.flow.MutableStateFlow

class NetworkListener: ConnectivityManager.NetworkCallback() {

    private val isNetworkAvailable = MutableStateFlow(false)

    fun checkNetworkAvailability(context: Context): MutableStateFlow<Boolean> {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(this)

        var isConnected = false

        val network = connectivityManager.activeNetwork
        Log.d("Network", "Active network!")
        if (network == null) {
            isConnected = false
        }
        else {
            var actNetwork = connectivityManager.getNetworkCapabilities(network)
            if (actNetwork == null) {
                isConnected = false
            }
            else {
                when {
                    actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Log.d("Network", "Wifi connected!")
                        isConnected = true
                    }
                    actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Log.d("Network", "Cellular network connected!")
                        isConnected = true
                    }
                    else -> {
                        Log.d("Network", "Internet not connected")
                        isConnected = false
                    }
                }
            }
        }
        isNetworkAvailable.value = isConnected
        return isNetworkAvailable
    }

    override fun onAvailable(network: Network) {
        isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        isNetworkAvailable.value = false
    }
}