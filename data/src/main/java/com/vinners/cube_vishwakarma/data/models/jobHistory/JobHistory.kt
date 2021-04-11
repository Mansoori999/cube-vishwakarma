package com.vinners.cube_vishwakarma.data.models.jobHistory

import com.google.gson.annotations.SerializedName

data class JobHistory(

    @SerializedName("jobid")
    val jobId: Int? = null,

    @SerializedName("logo")
    val logo: String? = null,

    @SerializedName("clientname")
    val clientName: String? = null,

    @SerializedName("jobno")
    val jobNo: String? = null,

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("workduration")
    val workDuration: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("status")
    val status: String? = null
) {
}