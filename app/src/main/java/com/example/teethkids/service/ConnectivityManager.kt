package com.example.teethkids.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData



// Classe para controlar o status da internet do dispositivo.
class ConnectivityManager(private val context: Context) : LiveData<Boolean>() {

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback


    // Sobrescreve o método onActive() para registrar o callback quando há observadores ativos
    override fun onActive() {
        super.onActive()
        registerNetworkCallback()
    }


    // Sobrescreve o método onInactive() para remover o callback quando não há observadores ativos
    override fun onInactive() {
        super.onInactive()
        unregisterNetworkCallback()
    }


    // Registra o callback para receber notificações sobre a disponibilidade e perda de redes
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

    // Remove o callback de notificação de rede
    private fun unregisterNetworkCallback() {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    // Verifica se há conectividade com a Internet sem live data.
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
