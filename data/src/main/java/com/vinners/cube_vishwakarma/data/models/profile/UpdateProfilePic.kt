package com.vinners.cube_vishwakarma.data.models.profile

import com.google.gson.annotations.SerializedName

data class UpdateProfilePic(
    @SerializedName("pic")
    val profilePic: String? = null
) {
}