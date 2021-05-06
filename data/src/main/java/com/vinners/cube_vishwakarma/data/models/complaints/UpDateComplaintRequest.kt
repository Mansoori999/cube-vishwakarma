package com.vinners.cube_vishwakarma.data.models.complaints

import com.google.gson.annotations.SerializedName

data class UpDateComplaintRequest(
        @SerializedName("statusremarks")
        val statusremarks:String ?= null,

        @SerializedName("status")
        val status:String ?= null,

        @SerializedName("id")
        val id: Int ?= null
) {
}