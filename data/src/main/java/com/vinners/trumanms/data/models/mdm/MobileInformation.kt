package com.vinners.trumanms.data.models.mdm

import com.google.gson.annotations.SerializedName

data  class MobileInformation (

    @SerializedName("deviceInformation")
    var deviceInformation: DeviceInformation? = null,

    @SerializedName("networkInfo")
    var networkInfo: NetworkInfo? = null,

    @SerializedName("osInformation")
    var osInformation: OsInformation? = null,

    @SerializedName("deviceType")
    val deviceType: String = "android",

    @SerializedName("deviceId")
    var deviceId: String? = null,

    @SerializedName("appVersion")
    var appVersion: String? = null
)
