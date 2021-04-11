package com.vinners.trumanms.data.models.profile

import com.google.gson.annotations.SerializedName

data class AppVersion(
    @SerializedName("isallowed")
    val isAllowed: Boolean
) {
}