package com.vinners.cube_vishwakarma.data.models.complaints

import com.google.gson.annotations.SerializedName

data class MyComplaintList (

        @SerializedName("complaintid")
        val complaintid : String? =null,

        @SerializedName("id")
        val id : String? =null,

        @SerializedName("fordate")
        val fordate : String? =null,

        @SerializedName("outlet")
        val outlet : String? =null,

        @SerializedName("customercode")
        val customercode : String? =null,

        @SerializedName("letterstatus")
        val letterstatus : String? =null,

        @SerializedName("status")
        val status : String? =null


) {
}