package com.vinners.cube_vishwakarma.data.models.jobs

import com.google.gson.annotations.SerializedName

data class ApplicationWithdraw(

    @SerializedName("applicationid")
    val applicationId: String,

    @SerializedName("withdrawReason")
    val withdrawReason: String? = null
) {
}