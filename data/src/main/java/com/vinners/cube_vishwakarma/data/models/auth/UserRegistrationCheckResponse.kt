package com.vinners.cube_vishwakarma.data.models.auth

import com.google.gson.annotations.SerializedName


data class UserRegistrationCheckResponse(

    @SerializedName("isOtpVerified")
    val isOtpVerified: Boolean = false,

    @SerializedName("isRegister")
    val isUserRegistered: Boolean = false,

    @SerializedName("firstName")
    val userName: String? = null,

    @SerializedName("otpToken")
    val otpToken: String? = null
) {

    val hasUserFilledRegistrationForm = userName == null
}