package com.vinners.cube_vishwakarma.data.models.outlets

import com.google.gson.annotations.SerializedName

data class EditOutletRequest(
    @field:SerializedName("secondarymail")
    val secondarymail: String?=null,


//    @field:SerializedName("gpsaddress")
//    val gpsaddress: String,

    @field:SerializedName("secondarymobile")
    val secondarymobile: String?=null,


    @field:SerializedName("outletid")
    val outletid: String ?= null,

    @field:SerializedName("pic")
    val pic: String ? = null,

//    @field:SerializedName("gps")
//    val gps: String

) {
}