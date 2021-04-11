package com.vinners.trumanms.data.models.auth

import com.google.gson.annotations.SerializedName


data class LoginWithOtpRequest(

    @SerializedName("mobile")
    val mobile : String? = null,

    @SerializedName("appHash")
    val appHash : String?
)