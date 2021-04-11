package com.vinners.trumanms.data.models.jobs

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

data class Job(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("jobno")
    val jobno: String? = null,

    @SerializedName("qualification")
    val qualification: String? = null,

    @SerializedName("experience")
    val experience: String? = null,

    @SerializedName("startdate")
    val startdate: String? = null,

    @SerializedName("enddate")
    val enddate: String? = null,

    @SerializedName("twowheelermust")
    val twowheelermust: Boolean,

    @SerializedName("certificateissue")
    val certificateissue: Boolean,

    @SerializedName("workfromhome")
    val workfromhome: Boolean,

    @SerializedName("lastdatetoapply")
    val lastDatetoApply: String? = null,

    @SerializedName("agelimit")
    val agelimit: Int? = null,

    @SerializedName("category")
    val category: String? = null,

    @SerializedName("jobtype")
    val jobtype: String? = null,

    @SerializedName("client")
    val client: String? = null,

    @SerializedName("logo")
    val logo: String? = null,

    @SerializedName("createdon")
    val jobDate: String? = null,

    @SerializedName("campName")
    val campName: String? = null,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("createdby")
    val createdby: String? = null,

    @SerializedName("cityName")
    val cityName: String? = null,

    @SerializedName("minsalary")
    val minSalary: Int? = null,

    @SerializedName("maxsalary")
    val maxSalary: Int? = null,

    @SerializedName("state")
    val state: String? = null,

    @SerializedName("isapplied")
    val isapplied: String? = null,

    @SerializedName("earningmode")
    val earningmode: String? = null,

    @SerializedName("earningpertask")
    val earningpertask: String? = null
) {
    companion object {

        private const val DATE_FORMAT = "yyyy-MM-dd"
        private val dateFormatter = SimpleDateFormat(DATE_FORMAT)
    }

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