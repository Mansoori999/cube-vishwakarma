package com.vinners.cube_vishwakarma.data.models.profile


import com.google.gson.annotations.SerializedName
import java.util.*

data class Profile(
        @SerializedName("id")
        val id: String? = null,

        @SerializedName("name")
        val name: String? = null,

        @SerializedName("mobile")
        val mobile: String? = null,

        @SerializedName("nickname")
        val nickname: String? = null,

        @SerializedName("alternatemobile")
        val alternatemobile: String? = null,

        @SerializedName("email")
        val email: String? = null,

        @SerializedName("logintype")
        val logintype: String? = null,

        @SerializedName("designation")
        val designation: String? = null,

        @SerializedName("loginid")
        val loginid: String? = null,


        @SerializedName("pic")
        val pic: String? = null,

        @SerializedName("employment")
        val employment: String? = null,

        @SerializedName("usertype")
        val userType: String? = null,

        @SerializedName("managerid")
        val managerid: String? = null,

        @SerializedName("deviceid")
        val deviceid: String? = null,

        @SerializedName("doj")
        val doj: String? = null,

        @SerializedName("dol")
        val dol: String? = null,

        @SerializedName("gender")
        val gender: String? = null,

        @SerializedName("dob")
        val dob: String? = null,

        @SerializedName("education")
        val education: String? = null,

        @SerializedName("aadhaarno")
        val aadhaarno: String? = null,

        @SerializedName("aadhaarpic")
        val aadhaarpic: String? = null,

        @SerializedName("pan")
        val pan: String? = null,

        @SerializedName("panpic")
        val panpic: String? = null,

        @SerializedName("dlnumber")
        val dlnumber: String? = null,

        @SerializedName("dlpic")
        val dlpic: String? = null,

        @SerializedName("pfnumber")
        val pfnumber: String? = null,

        @SerializedName("esicnumber")
        val esicnumber: String? = null,

        @SerializedName("voterid")
        val voterid: String? = null,

        @SerializedName("voteridpic")
        val voteridpic: String? = null,


        @SerializedName("bankname")
        val bankname: String? = null,

        @SerializedName("nameonbank")
        val nameonbank: String? = null,

        @SerializedName("ifsc")
        val ifsc: String? = null,

        @SerializedName("bankbranch")
        val bankbranch: String? = null,

        @SerializedName("accountno")
        val accountno: String? = null,

        @SerializedName("emergencymobile")
        val emergencymobile: String? = null,

        @SerializedName("emergencyname")
        val emergencyname: String? = null,

        @SerializedName("emergencyrelation")
        val emergencyrelation: String? = null,

        @SerializedName("referencename")
        val referencename: String? = null,

        @SerializedName("referencemobile")
        val referencemobile: String? = null,

        @SerializedName("referencerelation")
        val referencerelation: String? = null,

        @SerializedName("address")
        val address: String? = null,

        @SerializedName("pincode")
        val pincode: String? = null,

        @SerializedName("city")
        val city: String? = null,

        @SerializedName("state")
        val state: String? = null,

        @SerializedName("createdon")
        val createdon: String? = null,

        @SerializedName("createdby")
        val createdby: String? = null,

        @SerializedName("empcode")
        val empcode: String? = null
)