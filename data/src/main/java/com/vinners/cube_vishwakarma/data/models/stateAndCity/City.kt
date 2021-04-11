package com.vinners.cube_vishwakarma.data.models.stateAndCity

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("name")
    val cityName: String,

    @SerializedName("id")
    val cityId: String? = null
) {
    override fun toString(): String {
        return cityName!!
    }
}