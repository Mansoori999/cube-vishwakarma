package com.vinners.trumanms.data.models.auth

import com.google.gson.annotations.SerializedName


data class UserRegistrationCheckRequest(

    @SerializedName("mobile")
    val mobile: String,

    @SerializedName("appHash")
    val appHash: String? = null
)