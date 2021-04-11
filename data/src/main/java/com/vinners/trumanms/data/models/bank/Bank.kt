package com.vinners.trumanms.data.models.bank

import com.google.gson.annotations.SerializedName

data class Bank(

    @SerializedName("bankName")
    val bankName: String? = null,

    @SerializedName("bankBranch")
    val bankBranch: String? = null,

    @SerializedName("accountNo")
    val accountNo: String? = null,

    @SerializedName("nameOnBank")
    val nameOnBank: String? = null,

    @SerializedName("ifsc")
    val ifsc: String? = null,

    @SerializedName("adharNo")
    val adharNo: String? = null,

    @SerializedName("panNo")
    val panNo: String? = null
) {
}