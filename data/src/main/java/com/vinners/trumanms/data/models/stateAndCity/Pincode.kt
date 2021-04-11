package com.vinners.trumanms.data.models.stateAndCity

import com.google.gson.annotations.SerializedName

data class Pincode(
    @SerializedName("id")
    val pincodeId: String? = null,

    @SerializedName("name")
    val pincode: String
) {
    override fun toString(): String {
        return pincode!!
    }
}