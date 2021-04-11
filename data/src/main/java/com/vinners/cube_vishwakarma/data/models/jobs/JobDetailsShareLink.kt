package com.vinners.cube_vishwakarma.data.models.jobs

import com.google.gson.annotations.SerializedName

data class JobDetailsShareLink(
    @SerializedName("jobid")
    val jobId: String,

    @SerializedName("jobtitle")
    val jobTitle: String? = null,

    @SerializedName("city")
    val cityName: String? = null,

    @SerializedName("companyname")
    val companyName: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("packagename")
    val packageName: String? = null
) {
}