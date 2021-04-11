package com.vinners.trumanms.data.models.auth

import com.google.gson.annotations.SerializedName


data class RegisterResponse(
    @SerializedName("firstname")
    val firstName: String
)