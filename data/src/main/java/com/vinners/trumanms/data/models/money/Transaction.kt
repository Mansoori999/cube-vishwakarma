package com.vinners.trumanms.data.models.money

import com.google.gson.annotations.SerializedName

data class Transaction(

    @SerializedName("amount")
    val amount: Float? = null,

    @SerializedName("remarks")
    val remarks: String? = null,

    @SerializedName("date")
    val date: String? = null,

    @SerializedName("types")
    val type: String,

    @SerializedName("category")
    val category: String? = null,

    @SerializedName("firstname")
    val firstName: String? = null,

    @SerializedName("lastname")
    val lastName: String? = null,

    @SerializedName("mobile")
    val mobile: String? = null,

    @SerializedName("jobno")
    val jobNo: String? = null,

    @SerializedName("isreedemable")
    val isReedemable: Boolean
) {

}