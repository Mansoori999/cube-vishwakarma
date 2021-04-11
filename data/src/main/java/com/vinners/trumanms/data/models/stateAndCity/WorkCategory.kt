package com.vinners.trumanms.data.models.stateAndCity

import com.google.gson.annotations.SerializedName

data class WorkCategory(
    @SerializedName("name")
    val work: String? = null,

    @SerializedName("id")
    val workId: String? = null,

    @SerializedName("isactive")
    val isActive: Boolean,

    var isBoxChecked: Boolean
) {
    override fun toString(): String {
        return work!!
    }
}