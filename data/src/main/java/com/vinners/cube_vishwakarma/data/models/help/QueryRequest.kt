package com.vinners.cube_vishwakarma.data.models.help

import com.google.gson.annotations.SerializedName

data class QueryRequest(
    @SerializedName("question")
    val question: String? = null,

    @SerializedName("category")
    val category: String? = null
) {
}