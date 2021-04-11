package com.vinners.trumanms.data.models.money

import com.google.gson.annotations.SerializedName

data class Money(
    @SerializedName("totalearned")
    val totalEarned: Float? = null,

    @SerializedName("redeemed")
    val available: Float? = null,

    @SerializedName("redeemable")
    val reedemable: Float? = null
) {
}