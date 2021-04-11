package com.vinners.cube_vishwakarma.data.models.auth

import com.google.gson.annotations.SerializedName


data class RegisterResponse(
    @SerializedName("firstname")
    val firstName: String
)