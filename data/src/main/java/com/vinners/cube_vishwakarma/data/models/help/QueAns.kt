package com.vinners.cube_vishwakarma.data.models.help

import com.google.gson.annotations.SerializedName

data class QueAns(
    @SerializedName("question")
    val question: String,

    @SerializedName("answer")
    val answer: String
) {
}