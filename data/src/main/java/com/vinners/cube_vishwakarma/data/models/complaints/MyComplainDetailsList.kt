package com.vinners.cube_vishwakarma.data.models.complaints

import com.google.gson.annotations.SerializedName

data class MyComplainDetailsList (

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
    val status : String? =null,

    @SerializedName("statusremarks")
    val statusremarks: String? = null,

    @SerializedName("work")
    val work: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("outletname")
    val outletname: String? = null,

    @SerializedName("regionaloffice")
    val regionaloffice: String? = null,

    @SerializedName("salesarea")
    val salesarea: String? = null,

    @SerializedName("district")
    val district: String? = null,

    @SerializedName("subadmin")
    val subadmin: String? = null,

    @SerializedName("supervisor")
    val supervisor: String? = null,

    @SerializedName("foreman")
    val foreman: String? = null,

    @field:SerializedName("enduser")
    val enduser: String? = null,

    @SerializedName("orderBy")
    val orderBy: String? = null,

    @SerializedName("remarks")
    val remarks: String? = null

    ){
}