package com.vinners.trumanms.data.models.jobs

import com.google.gson.annotations.SerializedName

data class Application(
    @SerializedName("id")
    val id : Int,

    @SerializedName("iswithdraw")
    val iswithdraw : Boolean,

    @SerializedName("withdrawreason")
    val withdrawreason : String,

    @SerializedName("isaccepted")
    val isaccepted : Int,

    @SerializedName("jobid")
    val jobid : Int,

    @SerializedName("title")
    val title : String,

    @SerializedName("jobno")
    val jobno : String,

    @SerializedName("qualification")
    val qualification : String,

    @SerializedName("experience")
    val experience : String,

    @SerializedName("startdate")
    val startdate : String,

    @SerializedName("enddate")
    val enddate : String,

    @SerializedName("twowheelermust")
    val twowheelermust : Boolean,

    @SerializedName("certificateissue")
    val certificateissue : Boolean,

    @SerializedName("workfromhome")
    val workfromhome : Boolean,

    @SerializedName("lastdatetoapply")
    val lastdatetoapply : String,

    @SerializedName("agelimit")
    val agelimit : Int,

    @SerializedName("category")
    val category : String,

    @SerializedName("jobtype")
    val jobtype : String,

    @SerializedName("client")
    val client : String,

    @SerializedName("logo")
    val logo : String? = null,

    @SerializedName("createdon")
    val createdon : String,

    @SerializedName("campName")
    val campName : String,

    @SerializedName("address")
    val address : String,

    @SerializedName("createdby")
    val createdby : String,

    @SerializedName("jobstatus")
    val jobStatus: String? = null,

    @SerializedName("cityname")
    val cityName: String? = null,

    @SerializedName("istrained")
    val isTrained: Boolean,

    @SerializedName("state")
    val state: String? = null,

    @SerializedName("earningmode")
    val earningmode: String? = null,

    @SerializedName("earningpertask")
    val earningpertask: String? = null,

    @SerializedName("minsalary")
    val minSalary: Int? = null,


    @SerializedName("maxsalary")
    val maxSalary: Int? = null,

    ) {
}