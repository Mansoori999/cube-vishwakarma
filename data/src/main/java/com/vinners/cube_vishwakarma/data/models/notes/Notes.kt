package com.vinners.cube_vishwakarma.data.models.notes

import com.google.gson.annotations.SerializedName

data class Notes(
    @SerializedName("name")
    val name: String,

    @SerializedName("createdon")
    val date: String? = null,

    @SerializedName("notes")
    val notes: String? = null,

    @SerializedName("userdetail")
    val userDetail: String? = null
) {
}