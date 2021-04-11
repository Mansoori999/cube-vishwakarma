package com.vinners.trumanms.data.models.mdm

import com.google.gson.annotations.SerializedName


class DeviceInformation {

    @SerializedName("brandName")
    private var brandName: String? = null

    @SerializedName("modelName")
    private var modelName: String? = null

    @SerializedName("fullDeviceName")
    private var fullDeviceName: String? = null

    @SerializedName("tablet")
    private var tablet: Boolean = false

    @SerializedName("totalRam")
    private var totalRam: String? = null

    @SerializedName("totalStorage")
    private var totalStorage: String? = null

    @SerializedName("deviceScreenSize")
    private var deviceScreenSize: Double = 0.toDouble()

    fun setBrandName(brandName: String) {
        this.brandName = brandName
    }

    fun setFullDeviceName(fullDeviceName: String) {
        this.fullDeviceName = fullDeviceName
    }

    fun setModelName(modelName: String) {
        this.modelName = modelName
    }

    fun setTablet(tablet: Boolean) {
        this.tablet = tablet
    }

    fun setTotalRam(totalRam: String) {
        this.totalRam = totalRam
    }

    fun setTotalStorage(totalStorage: String) {
        this.totalStorage = totalStorage
    }

    fun setDeviceScreenSize(deviceScreenSize: Double) {
        this.deviceScreenSize = deviceScreenSize
    }
}
