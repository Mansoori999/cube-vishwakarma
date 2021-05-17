package com.vinners.cube_vishwakarma.data.models.outlets

import com.google.gson.annotations.SerializedName

data class ComplaintRequest(

    @SerializedName("outletid")
    val outletid : String
)