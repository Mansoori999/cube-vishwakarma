package com.vinners.trumanms.data.models.mdm

import com.google.gson.annotations.SerializedName


class OsInformation {

    @SerializedName("versionCode")
    private var versionCode: Int = 0

    @SerializedName("osVersionName")
    private var osVersionName: String? = null

    @SerializedName("deviceRooted")
    private var deviceRooted: Boolean = false

    fun setVersionCode(versionCode: Int) {
        this.versionCode = versionCode
    }

    fun setOsVersionName(osVersionName: String) {
        this.osVersionName = osVersionName
    }

    fun setDeviceRooted(deviceRooted: Boolean) {
        this.deviceRooted = deviceRooted
    }
}
