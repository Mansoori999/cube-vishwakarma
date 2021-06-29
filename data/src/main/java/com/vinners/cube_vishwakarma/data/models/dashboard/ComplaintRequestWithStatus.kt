package com.vinners.cube_vishwakarma.data.models.dashboard

import com.google.gson.annotations.SerializedName

data class ComplaintRequestWithStatus (
    @SerializedName("status")
    val status : String,

    @SerializedName("startDate")
    val startDate : String,

    @SerializedName("endDate")
    val endDate : String,

    @SerializedName("regionalOfficeIds")
    val regionalOfficeIds : String? = null,

    @SerializedName("subadminIds")
    val subadminIds:String? = null



){
}