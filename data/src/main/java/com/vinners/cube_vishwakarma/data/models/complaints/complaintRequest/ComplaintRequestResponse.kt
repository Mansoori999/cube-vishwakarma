package com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest

import com.google.gson.annotations.SerializedName

data class ComplaintRequestResponse (

    @SerializedName("zone")
    val zone : String? =null,

    @SerializedName("id")
    val id : String? =null,

    @SerializedName("fordate")
    val fordate : String? =null,

    @SerializedName("outlet")
    val outlet : String? =null,

    @SerializedName("customercode")
    val customercode : String? =null,

    @SerializedName("district")
    val district : String? =null,

    @SerializedName("status")
    val status : String? =null


){
}