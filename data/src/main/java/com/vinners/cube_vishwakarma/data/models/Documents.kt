package com.vinners.cube_vishwakarma.data.models

import com.google.gson.annotations.SerializedName

data class Documents(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("path")
    val filePath: String? = null,

    @SerializedName("category")
    val categoryName: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("isactive")
    val isActive: Boolean,

    var isDownloaded: Boolean
) {
}