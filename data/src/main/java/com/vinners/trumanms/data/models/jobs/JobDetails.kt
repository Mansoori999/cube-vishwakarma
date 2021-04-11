package com.vinners.trumanms.data.models.jobs

import com.google.gson.annotations.SerializedName

data class JobDetails(
    @SerializedName("id")
    val id: Int,

    @SerializedName("saveJobId")
    val saveJobId: Int,

    @SerializedName("campid")
    val campid: Int,

    @SerializedName("campaignNo")
    val campaignNo: String,

    @SerializedName("jobno")
    val jobno: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("catid")
    val catid: Int,

    @SerializedName("clientid")
    val clientid: Int,

    @SerializedName("agelimit")
    val agelimit: Int,

    @SerializedName("priority")
    val priority: Int,

    @SerializedName("minqualification")
    val minqualification: String,

    @SerializedName("minexperience")
    val minexperience: String,

    @SerializedName("typeid")
    val typeid: Int,

    @SerializedName("startdate")
    val startdate: String? = null,

    @SerializedName("enddate")
    val enddate: String,

    @SerializedName("reflink")
    val reflink: String,

    @SerializedName("state")
    val state: String? = null,

    @SerializedName("resourcecount") val resourcecount: Int? = null,
    @SerializedName("lastdatetoapply") val lastdatetoapply: String? = null,
    @SerializedName("twowheelermust") val twowheelermust: Boolean,
    @SerializedName("earningperjob") val earningperjob: Int,
    @SerializedName("earningpertask") val earningpertask: Int? = null,
    @SerializedName("certificateissue") val certificateissue: Boolean,
    @SerializedName("workfromhome") val workfromhome: Boolean,
    @SerializedName("aboutbrand") val aboutbrand: String,
    @SerializedName("status") val status: String? =  null,
    @SerializedName("isapproved") val isapproved: Boolean,
    @SerializedName("approvedon") val approvedon: String,
    @SerializedName("approvedby") val approvedby: String,
    @SerializedName("createdon") val createdon: String,
    @SerializedName("createdby") val createdby: Int,
    @SerializedName("updatedon") val updatedon: String,
    @SerializedName("updatedby") val updatedby: String,
    @SerializedName("city") val city: String,
    @SerializedName("jobcategory") val jobcategory: String,
    @SerializedName("campaignname") val campaignname: String,
    @SerializedName("brandname") val brandname: String,
    @SerializedName("clientname") val clientname: String,
    @SerializedName("logo") val logo: String? = null,
    @SerializedName("isapplied") val isapplied: Int,
    @SerializedName("issaved") val issaved: Int,
    @SerializedName("workperiod")
    val workPeriod: String? = null,

    @SerializedName("earningmode")
    val earningMode: String? = null,

    @SerializedName("minsalary")
    val minSalary: Int?,

    @SerializedName("maxsalary")
    val maxSalary: Int?,

    @SerializedName("paymentsource")
    val paymentSource: String? = null
) {
}