package com.vinners.trumanms.data.models.jobs

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class AppliedJob(

    @SerializedName("id")
    val applicationId: String,

    @SerializedName("jobid")
    val jobId: String,

    @SerializedName("logo")
    val companyIcon: String? = null,

    @SerializedName("client")
    val companyName: String,

    @SerializedName("address")
    val jobLocation: String,

    @SerializedName("jobtype")
    val position: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("twowheelermust")
    val bikeRequired: Boolean = false,

    @SerializedName("certificateissue")
    val certificationRequired: Boolean = false,

    @SerializedName("workfromhome")
    val canWorkFromHome: Boolean = false,

    val amountRange: String? = null,

    @SerializedName("createdon")
    val jobDate: String? = null,

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

        private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        private val dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
    }

    val appliedDateEstimate: String
        get() {

            if (jobDate == null)
                return "N/A"
            else {
                val jobDateTime = LocalDateTime.parse(jobDate, dateFormatter)
                val jobDate = jobDateTime.toLocalDate()
                val currentDate = LocalDate.now()
                val currentTime = LocalTime.now()

                return if (jobDate.isEqual(currentDate)) {
                    // Of Todays
                    val timeDiff = jobDateTime.hour - currentTime.hour
                    "$timeDiff Hours Ago "
                } else {
                    //Past date
                    val daysDiff = jobDate.dayOfMonth - currentDate.dayOfMonth
                    "$daysDiff Days Ago "
                }
            }
        }
}