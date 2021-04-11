package com.vinners.cube_vishwakarma.data.models.profile

import com.google.gson.annotations.SerializedName

data class ProfileDetails (
    @SerializedName("id") val id : Int,
    @SerializedName("title") val title : String,
    @SerializedName("firstname") val firstname : String,
    @SerializedName("lastname") val lastname : String,
    @SerializedName("usertype") val usertype : String,
    @SerializedName("mobile") val mobile : String,
    @SerializedName("empcode") val empcode : String,
    @SerializedName("whatsappmobile") val whatsappmobile : String,
    @SerializedName("email") val email : String,
    @SerializedName("dob") val dob : String,
    @SerializedName("dol") val dol : String,
    @SerializedName("gender") val gender : String,
    @SerializedName("education") val education : String,
    @SerializedName("experience") val experience : String,
    @SerializedName("languages") val languages : String,
    @SerializedName("twowheeler") val twowheeler : Boolean,
    @SerializedName("cityid") val cityid : Int,
    @SerializedName("city") val city : String,
    @SerializedName("pincodeid") val pincodeid : Int,
    @SerializedName("pincode") val pincode : Int,
    @SerializedName("yearsinbusiness") val yearsinbusiness : String,
    @SerializedName("agencyname") val agencyname : String,
    @SerializedName("teamsize") val teamsize : String,
    @SerializedName("websitepage") val websitepage : String,
    @SerializedName("facebookpage") val facebookpage : String,
    @SerializedName("agencytype") val agencytype : String,
    @SerializedName("designation") val designation : String,
    @SerializedName("businesscard") val businesscard : String,
    @SerializedName("workcategory") val workcategory : String,
    @SerializedName("gpsaddress") val gpsaddress : String,
    @SerializedName("gps") val gps : String,
    @SerializedName("bankname") val bankname : String,
    @SerializedName("accountno") val accountno : String,
    @SerializedName("nameonbank") val nameonbank : String,
    @SerializedName("bankbranch") val bankbranch : String,
    @SerializedName("ifsc") val ifsc : String,
    @SerializedName("adhar") val adhar : String? = null,
    @SerializedName("adharpic") val adharpic : String? = null,
    @SerializedName("pan") val pan : String? = null,
    @SerializedName("panpic") val panpic : String? = null,
    @SerializedName("isotpverified") val isotpverified : Boolean,
    @SerializedName("createdon") val createdon : String,
    @SerializedName("createdby") val createdby : String,
    @SerializedName("districtid") val districtid : Int,
    @SerializedName("stateid") val stateid : Int,
    @SerializedName("state") val state : String
){
}