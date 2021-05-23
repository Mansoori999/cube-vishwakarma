package com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest

import com.google.gson.annotations.SerializedName

data class ComplaintSubmitRequest(
        @field:SerializedName("typeid")
        val typeid: String? = null,

        @field:SerializedName("work")
        val work: String? = null,

        @field:SerializedName("outletid")
        val outletid: Int? = null,

        @field:SerializedName("remarks")
        val remarks: String? = null,

        @field:SerializedName("orderby")
        val orderby: String? = null


) {
}