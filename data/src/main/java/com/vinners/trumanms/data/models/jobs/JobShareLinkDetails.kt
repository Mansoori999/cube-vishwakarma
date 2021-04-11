package com.vinners.trumanms.data.models.jobs

import com.google.gson.annotations.SerializedName

class JobShareLinkDetails(
    @SerializedName("url")
    val url: String,

    @SerializedName("msg")
    val message: String
) {
}