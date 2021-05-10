package com.vinners.cube_vishwakarma.data.models.outlets

import com.google.gson.annotations.SerializedName

data class OutletDetailsList (
    @SerializedName("outletid")
    val outletid : String? =null,

    @SerializedName("outlet")
    val outlet : String? =null,

    @SerializedName("customercode")
    val customercode : String? =null,

    @SerializedName("district")
    val district : String? =null,

    @SerializedName("location")
    val location : String? =null,

    @SerializedName("address")
    val address : String? =null,

    @SerializedName("category")
    val category : String? =null,

    @SerializedName("contactperson")
    val contactperson : String? =null,

    @SerializedName("primarymobile")
    val primarymobile : String? =null,

    @SerializedName("secondarymobile")
    val secondarymobile : String? =null,

    @SerializedName("primarymail")
    val primarymail : String? =null,

    @SerializedName("secondarymail")
    val secondarymail : String? =null,

    @SerializedName("gps")
    val gps : String? =null,

    @SerializedName("gpsaddress")
    val gpsaddress : String? =null,

    @SerializedName("salesarea")
    val salesarea : String? =null,

    @SerializedName("said")
    val said : String? =null,

    @SerializedName("regionaloffice")
    val regionaloffice : String? =null,

    @SerializedName("roid")
    val roid : String? =null,

    @SerializedName("zone")
    val zone : String? =null,

    @SerializedName("zoneid")
    val zoneid : String? =null,

    @SerializedName("pic")
    val pic : String? =null,
) {
}