package com.example.teethkids.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData

//class ConnectivityManager(val context: Context) {
//

//}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class ConnectivityManager(private val context: Context) : LiveData<Boolean>() {

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        registerNetworkCallback()
    }

    override fun onInactive() {
        super.onInactive()
        unregisterNetworkCallback()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun registerNetworkCallback() {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                postValue(true)
            }

            override fun onLost(network: Network) {
                postValue(false)
            }
        }

        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    private fun unregisterNetworkCallback() {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    fun checkInternet(): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = connectivityManager.activeNetwork ?: return false

        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
