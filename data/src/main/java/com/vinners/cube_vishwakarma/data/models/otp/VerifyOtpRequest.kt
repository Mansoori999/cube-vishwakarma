package com.vinners.cube_vishwakarma.data.models.otp

import com.google.gson.annotations.SerializedName
import com.vinners.cube_vishwakarma.data.models.mdm.MobileInformation


data class VerifyOtpRequest (
    @SerializedName("otpToken")
    val otpToken : String? = null,

    @SerializedName("otp")
    val otp : String? = null,

    @SerializedName("mobileInfo")
    val mobileInfo : MobileInformation? = null,

    @SerializedName("referalCode")
    val referalCode: String? = null,

    @SerializedName("gpsAddress")
    val gpsAddress : String? = null,

    @SerializedName("gps")
    val gps : String? = null,
)