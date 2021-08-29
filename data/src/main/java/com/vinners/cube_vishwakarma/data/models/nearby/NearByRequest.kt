package com.vinners.cube_vishwakarma.data.models.nearby

import com.google.gson.annotations.SerializedName

data class NearByRequest(
    @field:SerializedName("gpsaddress")
    val gpsaddress: String? = null,

    @field:SerializedName("range")
    val range: String? = null,

    @field:SerializedName("type")
    val type: String? = null




)
