package com.vinners.trumanms.data.models.auth

import com.google.gson.annotations.SerializedName


data class LoginWithOtpResponse(

    @SerializedName("otpToken")
    val otpToken: String? = null
)