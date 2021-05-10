package com.vinners.cube_vishwakarma.data.models.outlets

import com.google.gson.annotations.SerializedName

data class OutletsList(
        @SerializedName("name")
        val name : String? =null,

        @SerializedName("customercode")
        val customercode : String? =null,

        @SerializedName("regionaloffice")
        val regionaloffice : String? =null,

        @SerializedName("salesarea")
        val salesarea : String? =null,

        @SerializedName("districtname")
        val districtname : String? =null,

        @SerializedName("id")
        val outletid : String? =null,

) {
}