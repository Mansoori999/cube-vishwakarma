package com.vinners.cube_vishwakarma.data.models.jobs

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

data class SavedJob(

    @SerializedName("id")
    val applicationId: String?,

    @SerializedName("jobid")
    val jobId: String,

    @SerializedName("logo")
    val companyIcon: String? = null,

    @SerializedName("client")
    val companyName: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("address")
    val jobLocation: String,

    @SerializedName("jobtype")
    val position: String,

    @SerializedName("twowheelermust")
    val bikeRequired: Boolean = false,

    @SerializedName("certificateissue")
    val certificationRequired: Boolean = false,

    @SerializedName("workfromhome")
    val canWorkFromHome: Boolean = false,

    val amountRange: String? = null,

    @SerializedName("createdon")
    val jobDate: String? = null,

    @SerializedName("lastdatetoapply")
    val lastDatetoApply: String? = null,

    @SerializedName("cityname")
    val cityName: String? = null,

    @SerializedName("minsalary")
    val minSalary: Int,

    @SerializedName("maxsalary")
    val maxSalary: Int,

    @SerializedName("state")
    val state: String? = null,

    @SerializedName("earningmode")
    val earningmode: String? = null,

    @SerializedName("earningpertask")
    val earningpertask: String? = null

) {

    companion object {

        private const val DATE_FORMAT = "yyyy-MM-dd"
        private val dateFormatter = SimpleDateFormat(DATE_FORMAT)
    }
    val hasAppliedForThisJob: Boolean get() = applicationId != null

    val appliedDateEstimate: String
    get() {

        if (lastDatetoApply == null)
            return "N/A"
        else {
            val jobDate = dateFormatter.parse(lastDatetoApply)
            val currentDate = Calendar.getInstance()

            return if (jobDate.equals(currentDate)) {
                // Of Todays
                val timeDiff = ((jobDate.time - currentDate.timeInMillis)/(1000 * 60 * 60)) % 24
                "$timeDiff Hours Ago "
            } else {
                //Past date
                val daysDiff = ((jobDate.time - currentDate.timeInMillis)/(1000 * 60 * 60 * 24)) % 365
                "${daysDiff.plus(1)} Days Left "
            }
        }
    }
}