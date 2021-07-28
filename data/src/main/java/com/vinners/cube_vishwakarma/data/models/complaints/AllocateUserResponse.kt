package com.vinners.cube_vishwakarma.data.models.complaints

import com.google.gson.annotations.SerializedName

data class AllocateUserResponse(
        @SerializedName("id")
        val id : String? =null,

        @SerializedName("name")
        val name : String? =null,

        @SerializedName("mobile")
        val mobile : String? =null,

        @SerializedName("designation")
        val designation : String? =null,
){
}