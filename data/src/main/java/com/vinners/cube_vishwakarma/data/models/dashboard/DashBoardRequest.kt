package com.vinners.cube_vishwakarma.data.models.dashboard

import com.google.gson.annotations.SerializedName

data class DashBoardRequest(
        @field:SerializedName("startDate")
        val startDate: String,

        @field:SerializedName("endDate")
        val endDate: String,

        @field:SerializedName("regionalOfficeids")
        val regionalOfficeids: String? = null
) {
}