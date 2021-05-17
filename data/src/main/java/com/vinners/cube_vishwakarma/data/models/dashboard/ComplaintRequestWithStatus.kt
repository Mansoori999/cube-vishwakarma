package com.vinners.cube_vishwakarma.data.models.dashboard

import com.google.gson.annotations.SerializedName

data class ComplaintRequestWithStatus (
    @SerializedName("status")
    val status : String,
){
}