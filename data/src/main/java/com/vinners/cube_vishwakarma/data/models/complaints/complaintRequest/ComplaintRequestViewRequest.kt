package com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest

import com.google.gson.annotations.SerializedName

data class ComplaintRequestViewRequest(
    @field:SerializedName("startDate")
    val startDate: String? = null,

    @field:SerializedName("endDate")
    val endDate: String? = null,
) {
}