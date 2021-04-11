package com.vinners.trumanms.data.models.auth

import com.google.gson.annotations.SerializedName
import com.vinners.trumanms.data.models.mdm.MobileInformation

data class LoginRequest(

    @SerializedName( "mobile")
    val userName : String? = null,

    @SerializedName("password")
    val password : String? = null,

    @SerializedName("appHash")
    val appHash : String?,

    @SerializedName("mobileInfo")
    val mobileInfo : MobileInformation? = null
)