package com.vinners.cube_vishwakarma.data.models.profile

import com.google.gson.annotations.SerializedName


data class UpdateProfileDataResponse(

    @SerializedName("name")
    val name: String,

    @SerializedName("mobile")
    val mobile: String,

    @SerializedName("email")
    val email: String?
)