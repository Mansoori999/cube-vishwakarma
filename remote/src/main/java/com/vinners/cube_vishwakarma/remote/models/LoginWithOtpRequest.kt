package com.vinners.cube_vishwakarma.remote.models

import com.google.gson.annotations.SerializedName

data class CheckUserRegistrationStatusRequest(

    @SerializedName("mobile")
    val mobile : String? = null,

    @SerializedName("appHash")
    val appHash : String? = null
)

data class CheckUserRegistrationStatusResponse(

    @SerializedName("isRegister")
    val isRegistered : Boolean,

    @SerializedName("userName")
    val userName: String? = null
)
