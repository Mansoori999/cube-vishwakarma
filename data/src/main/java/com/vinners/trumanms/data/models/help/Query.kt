package com.vinners.trumanms.data.models.help

import com.google.gson.annotations.SerializedName

data class Query(
    @SerializedName("category")
    val category: String? = null,

    @SerializedName("question")
    val question: String? = null,

    @SerializedName("answer")
    val answer: String? = null,

    @SerializedName("path")
    val path: String? = null,

    @SerializedName("fordate")
    val forDate: String? = null
) {
}