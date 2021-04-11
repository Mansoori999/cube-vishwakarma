package com.vinners.trumanms.data.models.profile


import com.google.gson.annotations.SerializedName
import java.util.*

data class Profile(
    @SerializedName("firstName")
    val firstName: String? = null,

    @SerializedName("lastName")
    val lastName: String? = null,

    @SerializedName("userType")
    val userType: String? = null,

    @SerializedName("agencyType")
    val agencyType: String? = null,

    @SerializedName("agencyname")
    val agencyName: String? = null,

    @SerializedName("mobile")
    val mobile: String? = null,

    @SerializedName("watsappMobile")
    val whatsAppMobileNumber: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("dob")
    val dob: String? = null,

    @SerializedName("gender")
    val gender: String? = null,

    @SerializedName("city")
    val district: String? = null,

    @SerializedName("cityId")
    val cityId: Int? = null,

    @SerializedName("password")
    val password: String,

    @SerializedName("pinCode")
    val pinCode: String? = null,

    @SerializedName("pinCodeId")
    val pinCodeId: Int? = null,

    @SerializedName("gpsAddress")
    val gpsAddress : String? = null,

    @SerializedName("education")
    val education : String? = null,

    @SerializedName("experience")
    val experience : String? = null,

    @SerializedName("languages")
    val languages : String? = null,

    @SerializedName("twoWheeler")
    val twoWheeler : Boolean,

    @SerializedName("workCategory")
    val workCategory : String? = null,

    @SerializedName("gps")
    val gps : String? = null,

    @SerializedName("designation")
    val designation : String? = null,

    @SerializedName("teamSize")
    val teamSize : String? = null,

    @SerializedName("websitePage")
    val websitePage : String? = null,

    @SerializedName("facebookPage")
    val facebookPage : String? = null,

    @SerializedName("yearsInBusiness ")
    val yearsInBusiness: String? = null,

    @SerializedName("bankname")
    val bankName: String? = null,

    @SerializedName("accountno")
    val accountNo: String? = null,

    @SerializedName("nameonbank")
    val nameOnBank: String? = null,

    @SerializedName("mobileonbank")
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

    val aadharNo: String? = null,

    val profilePicUpdated: Boolean = false
)