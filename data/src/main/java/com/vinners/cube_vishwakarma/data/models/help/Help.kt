package com.vinners.cube_vishwakarma.data.models.help

import com.google.gson.annotations.SerializedName

data class Help(
    @SerializedName("category")
    val category: String,

    @SerializedName("questionList")
    val questionList: List<QueAns>
) {
}