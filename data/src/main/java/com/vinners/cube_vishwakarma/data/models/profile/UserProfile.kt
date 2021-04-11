package com.vinners.cube_vishwakarma.data.models.profile

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("jobcCompleted")
    val jobCompleted: Int,

    @SerializedName("moneyEarned")
    val moneyEarned: Int,

    @SerializedName("rating")
    val rateing: String? = null
) {

}