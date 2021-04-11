package com.vinners.cube_vishwakarma.data.models.mdm

import com.google.gson.annotations.SerializedName


class NetworkInfo {

    @SerializedName("simCount")
    private var simCount: Int = 0

    @SerializedName("sim1ProviderNames")
    private var sim1ProviderNames: List<String>? = null

    @SerializedName("internetConnectionMode")
    private var internetConnectionMode: String? = null

    fun setSimCount(simCount: Int) {
        this.simCount = simCount
    }

    fun setSim1ProviderNames(sim1ProviderNames: List<String>) {
        this.sim1ProviderNames = sim1ProviderNames
    }

    fun setInternetConnectionMode(internetConnectionMode: String) {
        this.internetConnectionMode = internetConnectionMode
    }

}
