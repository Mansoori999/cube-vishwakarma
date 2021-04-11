package com.vinners.trumanms.data.models.otp

import com.google.gson.annotations.SerializedName
import com.vinners.trumanms.data.models.mdm.MobileInformation

data class VerifyOtpLoginRequest(
    @SerializedName("token")
    val otpToken : String? = null,

    @SerializedName("otp")
    val otp : String? = null,

    @SerializedName("mobileInfo")
    val mobileInfo : MobileInformation? = null,

    @SerializedName("gps")
    val gps : String? = null,

    @SerializedName("gpsAddress")
    val gpsAddress : String? = null
) {
}