package com.vinners.trumanms.data.models.otp

import com.google.gson.annotations.SerializedName

class OtpResponse(
    @SerializedName("isOtpVerified")
    val isOtpVerified: Boolean
) {
}