package com.vinners.trumanms.data.models.certificate

import com.google.gson.annotations.SerializedName

data class Certificate(

    @SerializedName("applicationid")
    val applicationid: Int,

    @SerializedName("clientname")
     val clientName: String? = null,

    @SerializedName("logo")
    val logo: String? = null,

    @SerializedName("jobno")
    val jobno: String? = null,

    @SerializedName("certificateno")
    val certificateNo: String,

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("workduration")
    val workDuration: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("certificateissuedate")
    val certificateIssueDate: String? = null,

    @SerializedName("filePath")
    val filePath: String? = null,

    var isDownloaded: Boolean
) {
}