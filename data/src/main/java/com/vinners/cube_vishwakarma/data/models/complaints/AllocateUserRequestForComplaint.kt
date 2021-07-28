package com.vinners.cube_vishwakarma.data.models.complaints

import com.google.gson.annotations.SerializedName

data class AllocateUserRequestForComplaint(
        @SerializedName("supervisorid")
        val supervisorid:String? = null,

        @SerializedName("enduserid")
        val enduserid:String? = null,

        @SerializedName("foremanid")
        val foremanid: String? = null,

        @SerializedName("compid")
        val compid: String
) {
}