package com.vinners.trumanms.data.models.bank

import com.google.gson.annotations.SerializedName

class BankDetails(
    @SerializedName("BRANCH")
    val branch: String? = null,
    @SerializedName("CENTRE")
    val centre: String? = null,
    @SerializedName("DISTRICT")
    val district: String? = null,
    @SerializedName("STATE")
    val state: String? = null,
    @SerializedName("ADDRESS")
    val address: String? = null,
    @SerializedName("CONTACT")
    val contact: String? = null,
    @SerializedName("MICR")
    val micr: String? = null,
    @SerializedName("UPI")
    val upi: String? = null,
    @SerializedName("RTGS")
    val rtgs: String? = null,
    @SerializedName("CITY")
    val city: String? = null,
    @SerializedName("NEFT")
    val neft: String? = null,
    @SerializedName("IMPS")
    val imps: String? = null,
    @SerializedName("BANK")
    val bank: String? = null,
    @SerializedName("BANKCODE")
    val bankCode: String? = null,
    @SerializedName("IFSC")
    val ifsc: String? = null
) {
}