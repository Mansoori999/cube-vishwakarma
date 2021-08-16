package com.vinners.cube_vishwakarma.data.models.auth

import com.google.gson.annotations.SerializedName


data class LoginRequest(

    @SerializedName( "mobile")
    val userName : String? = null,

    @SerializedName("password")
    val password : String? = null,

    @SerializedName("appHash")
    val appHash : String?

//    @SerializedName("mobileInfo")
//    val mobileInfo : MobileInformation? = null
)