package com.vinners.cube_vishwakarma.data.models.auth

import com.google.gson.annotations.SerializedName
import com.vinners.cube_vishwakarma.data.models.mdm.MobileInformation

data class LoginWithoutOtpRequest (
        @SerializedName( "loginid")
        val loginid : String? = null,

        @SerializedName("password")
        val password : String? = null,

        @SerializedName("mobileInfo")
        val mobileInfo  : MobileInformation?
){
}