package com.vinners.cube_vishwakarma.data.models.outlets

import com.google.gson.annotations.SerializedName

data class EditOutletLocation(

    @field:SerializedName("gpsaddress")
    val gpsaddress: String,

    @field:SerializedName("outletid")
    val outletid: String ?= null,


    @field:SerializedName("gps")
    val gps: String

) {
}