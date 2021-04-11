package com.vinners.cube_vishwakarma.data.models.stateAndCity

import com.google.gson.annotations.SerializedName

data class State(
    @SerializedName("name")
    val stateName: String,

    @SerializedName("id")
    val stateId: String? = null,

    var isChecked: Boolean = false
) {
    override fun toString(): String {
        return stateName
    }
}