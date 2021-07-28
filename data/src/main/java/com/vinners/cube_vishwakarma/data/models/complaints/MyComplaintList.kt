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

        @SerializedName("outletid")
        val outletid : String? =null,

        @SerializedName("customercode")
        val customercode : String? =null,

        @SerializedName("letterstatus")
        val letterstatus : String? =null,

        @SerializedName("status")
        val status : String? =null,

        @SerializedName("outletcategory")
        val outletcategory : String? =null,

        @SerializedName("regionaloffice")
        val regionaloffice : String? =null,

        @SerializedName("salesarea")
        val salesarea : String? =null,

        @SerializedName("work")
        val work : String? =null,

        @SerializedName("subadmin")
        val subadmin : String? =null,

        @SerializedName("orderby")
        val orderby : String? =null,

        @SerializedName("supervisor")
        val supervisor : String? =null,

        @SerializedName("enduser")
        val enduser : String? =null,







) {
}