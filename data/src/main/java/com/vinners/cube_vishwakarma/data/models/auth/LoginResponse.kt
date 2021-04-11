package com.vinners.cube_vishwakarma.data.models.auth

import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("id")
    val id: String,

    @SerializedName("empcode")
    val empcode: String? = null,

    @SerializedName("authToken")
    val authToken: String,

    @SerializedName("firstname")
    val firstName: String? = null,

    @SerializedName("lastname")
    val lastName: String,

    @SerializedName("usertype")
    val userType: String,

    @SerializedName("agencyType")
    val agencyType: String,

    @SerializedName("agencyname")
    val agencyName: String? = null,

    @SerializedName("mobile")
    val mobile: String,

    @SerializedName("watsappMobile")
    val whatsAppMobileNumber: String,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("dob")
    val dob: String? = null,

    @SerializedName("gender")
    val gender: String? = null,

    @SerializedName("district")
    val district: String? = null,

    @SerializedName("cityid")
    val cityId: Int? = null,

    @SerializedName("password")
    val password: String,

    @SerializedName("pincode")
    val pincode: String? = null,

    @SerializedName("pincodeid")
    val pinCodeId: Int? = null,

    @SerializedName("gpsAddress")
    val gpsAddress: String? = null,

    @SerializedName("education")
    val education: String? = null,

    @SerializedName("experience")
    val experience: String? = null,

    @SerializedName("languages")
    val languages: String? = null,

    @SerializedName("twoWheeler")
    val twoWheeler: Boolean,

    @SerializedName("workcategory")
    val workCategory: String? = null,

    @SerializedName("gps")
    val gps: String? = null,

    @SerializedName("designation")
    val designation: String? = null,

    @SerializedName("teamSize")
    val teamSize: String? = null,

    @SerializedName("websitePage")
    val websitePage: String? = null,

    @SerializedName("facebookPage")
    val facebookPage: String? = null,

    @SerializedName("yearsinbusiness")
    val yearsinbusiness: String? = null,

    @SerializedName("bankname")
    val bankName: String? = null,

    @SerializedName("accountno")
    val accountNo: String? = null,

    @SerializedName("nameonbank")
    val nameOnBank: String? = null,

    @SerializedName("bankbranch")
    val mobileOnBank: String? = null,

    @SerializedName("ifsc")
    val ifsc: String? = null,

    @SerializedName("districtid")
    val districtId: Int? = null,

    @SerializedName("stateid")
    val stateId: Int? = null,

    @SerializedName("state")
    val state: String? = null,

    @SerializedName("profilepic")
    val profilePic: String? = null,

    @SerializedName("adhar")
    val adhar: String? = null

) {
    val hasUserFilledRegistrationForm = firstName == null
}