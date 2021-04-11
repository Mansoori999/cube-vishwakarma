package com.vinners.trumanms.core.network

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class NetworkManager constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val KEY_CURRENT_NETWORK_HEALTH = "network_health"
    }

    private val mNetworkHealth: LiveData<NetWorkHealth> = MutableLiveData()
    val networkHealth get() = mNetworkHealth

    suspend fun getNetworkHealth(): NetWorkHealth {
        return NetWorkHealth.UNKNOWN
    }

    private val mNetworkConnectionStatus: LiveData<NetWorkConnectionStatus> = MutableLiveData()
    val networkConnectionStatus get() = mNetworkConnectionStatus

    suspend fun getNetworkConnectionStatus(): NetWorkConnectionStatus {
        return NetWorkConnectionStatus.UNKNOWN
    }
}