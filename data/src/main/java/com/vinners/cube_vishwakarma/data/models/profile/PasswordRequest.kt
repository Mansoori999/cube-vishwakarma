package com.vinners.cube_vishwakarma.data.models.profile

import com.google.gson.annotations.SerializedName

data class PasswordRequest(
        @SerializedName("newpassword")
        val newpassword: String,
) {
}