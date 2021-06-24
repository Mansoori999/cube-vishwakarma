package com.vinners.cube_vishwakarma.data.models.outlets

import com.google.gson.annotations.SerializedName
import com.vinners.cube_vishwakarma.data.models.mdm.MobileInformation


data class OutletsRequest(

        @SerializedName("name")
        val name : String? =null,

        @SerializedName("customercode")
        val customercode : String? =null,

        @SerializedName("regionaloffice")
        val regionaloffice : String? =null,

        @SerializedName("salesarea")
        val salesarea : String? =null,

        @SerializedName("roid")
        val roid : Int? =null,

        @SerializedName("said")
        val said : Int? =null,

        @SerializedName("districtname")
        val districtname : String? =null,

        @SerializedName("id")
        val outletid : String? =null,

        @SerializedName("gps")
        val gps : String? =null,
)