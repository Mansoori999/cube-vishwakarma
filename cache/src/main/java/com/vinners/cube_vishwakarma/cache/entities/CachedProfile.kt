package com.vinners.cube_vishwakarma.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = CachedProfile.TABLE_NAME)
data class CachedProfile(

    @ColumnInfo(name = COLUMN_FIRST_NAME)
    val name: String? = null,

    @ColumnInfo(name = COLUMN_NICK_NAME)
    val nickname: String? = null,

    @ColumnInfo(name = COLUMN_MOBILE_NUMBER)
    val mobile: String? = null,

    @ColumnInfo(name = COLUMN_ALTERNATE_MOBILE_NUMBER)
    val alternatemobile: String? = null,

    @ColumnInfo(name = COLUMN_EMAIL)
    var email: String? = null,

    @ColumnInfo(name = COLUMN_LOGINTYPE)
    var logintype: String? = null,

    @ColumnInfo(name = COLUMN_DESIGNATION_NAME)
    var designation: String? = null,

    @ColumnInfo(name = COLUMN_LOGINID)
    var loginid: String? = null,

    @ColumnInfo(name = COLUMN_PROFILE_PIC)
    val pic: String? = null,

    @ColumnInfo(name = COLUMN_EMPLOYMENT)
    val  employment: String? = null,

    @ColumnInfo(name = COLUMN_USER_TYPE)
    val userType: String? = null,

    @ColumnInfo(name = COLUMN_MANAGERID)
    val  managerid: String? = null,

    @ColumnInfo(name = COLUMN_DEVICEID)
    val  deviceid: String? = null,

    @ColumnInfo(name = COLUMN_DOJ)
    val  doj: String? = null,

    @ColumnInfo(name = COLUMN_DOL)
    val  dol: String? = null,

    @ColumnInfo(name = COLUMN_GENDER)
    val gender: String? = null,

    @ColumnInfo(name = COLUMN_DATE_OF_BIRTH)
    val dob: String? = null,

    @ColumnInfo(name = COLUMN_EDUCATION)
    val education : String? = null,

    @ColumnInfo(name = COLUMN_AADHAARNO)
    val  aadhaarno: String? = null,

    @ColumnInfo(name = COLUMN_AADHAARPIC)
    val  aadhaarpic: String? = null,

    @ColumnInfo(name = COLUMN_PAN)
    val  pan: String? = null,

    @ColumnInfo(name = COLUMN_PANPIC)
    val  panpic: String? = null,

    @ColumnInfo(name = COLUMN_DLNO)
    val  dlnumber: String? = null,

    @ColumnInfo(name = COLUMN_DLPIC)
    val  dlpic: String? = null,

    @ColumnInfo(name = COLUMN_PFNO)
    val  pfnumber: String? = null,

    @ColumnInfo(name = COLUMN_ESINO)
    val  esicnumber: String? = null,

    @ColumnInfo(name = COLUMN_VOTERID)
    val  voterid: String? = null,

    @ColumnInfo(name = COLUMN_VOTERPIC)
    val  voteridpic: String? = null,

    @ColumnInfo(name = COLUMN_BANK_NAME)
    val bankname: String? = null,


    @ColumnInfo(name = COLUMN_NAME_ON_BANK)
    val nameonbank: String? = null,

    @ColumnInfo(name = COLUMN_IFSC)
    val ifsc: String? = null,

    @ColumnInfo(name = COLUMN_BANKBRANCH)
    val  bankbranch: String? = null,

    @ColumnInfo(name = COLUMN_ACCOUNT_NO)
    val accountno: String? = null,

    @ColumnInfo(name = COLUMN_EMERGENCY_MOBILE)
    val  emergencymobile: String? = null,

    @ColumnInfo(name = COLUMN_EMERGENCY_NAME)
    val  emergencyname: String? = null,

    @ColumnInfo(name = COLUMN_EMERGENCY_RELATION)
    val  emergencyrelation: String? = null,

    @ColumnInfo(name = COLUMN_REFERENCE_NAME)
    val  referencename: String? = null,

    @ColumnInfo(name = COLUMN_REFERENCE_MOBILE)
    val  referencemobile: String? = null,

    @ColumnInfo(name = COLUMN_REFERENCE_RELATION)
    val  referencerelation: String? = null,

    @ColumnInfo(name = COLUMN_ADDRESS)
    var address: String? = null,

    @ColumnInfo(name = COLUMN_PIN_CODE)
    val pincode: String? = null,

    @ColumnInfo(name = COLUMN_CITY_NAME)
    var city: String? = null,

    @ColumnInfo(name = COLUMN_STATE)
    val state: String? = null,

    @ColumnInfo(name = COLUMN_CREATEDON)
    val  createdon: String? = null,

    @ColumnInfo(name = COLUMN_CREATEBY)
    val  createdby: String? = null,

) {

    companion object {
        @Ignore
        const val TABLE_NAME: String = "profile"

        @Ignore
        const val COLUMN_FIRST_NAME: String = "first_name"

        @Ignore
        const val COLUMN_NICK_NAME: String = "nick_name"

        @Ignore
        const val COLUMN_MOBILE_NUMBER: String = "mobile_number"

        @Ignore
        const val COLUMN_ALTERNATE_MOBILE_NUMBER: String = "alternate_mobile_number"

        @Ignore
        const val COLUMN_GENDER: String = "gender"

        @Ignore
        const val COLUMN_CITY_ID: String = "city_id"

        @Ignore
        const val COLUMN_CITY_NAME: String = "city_name"


        @Ignore
        const val COLUMN_EMAIL: String = "email"

        @Ignore
        const val COLUMN_LOGINTYPE: String = "logintype"


        @Ignore
        const val COLUMN_PIN_CODE: String = "pin_code"

        @Ignore
        const val COLUMN_DATE_OF_BIRTH: String = "dob"

        @Ignore
        const val COLUMN_ADDRESS: String = "address"

        @Ignore
        const val COLUMN_EDUCATION: String = "education"

        @Ignore
        const val COLUMN_AADHAARNO: String = "aadhaarno"

        @Ignore
        const val COLUMN_AADHAARPIC: String = "aadhaarpic"

        @Ignore
        const val COLUMN_PAN: String = "pan"

        @Ignore
        const val COLUMN_PFNO: String = "pfnumber"

        @Ignore
        const val COLUMN_VOTERID: String = "voterid"

        @Ignore
        const val COLUMN_VOTERPIC: String = "voteridpic"


        @Ignore
        const val COLUMN_ESINO: String = "esicnumber"

        @Ignore
        const val COLUMN_PANPIC: String = "panpic"

        @Ignore
        const val COLUMN_DLNO: String = "dlnumber"

        @Ignore
        const val COLUMN_DLPIC: String = "dlpic"

        @Ignore
        const val COLUMN_WHATS_APP_MOBILE: String = "whats_app_mobile"

        @Ignore
        const val COLUMN_PINCODE_ID: String = "pincode_id"

        @Ignore
        const val COLUMN_LANGUAGE: String = "language"

        @Ignore
        const val COLUMN_EXPERIENCE: String = "experience"

        @Ignore
        const val COLUMN_DESIGNATION_NAME: String = "designationName"

        @Ignore
        const val COLUMN_LOGINID: String = "loginid"



        @Ignore
        const val COLUMN_WEBSITE_PAGE: String = "websitePage"

        @Ignore
        const val COLUMN_FACEBOOK_PAGE: String = "facebookPage"

        @Ignore
        const val COLUMN_TEAM_SIZE: String = "teamSize"

        @Ignore
        const val COLUMN_TWO_WHEELAR: String = "twoWheelar"

        @Ignore
        const val COLUMN_USER_TYPE: String = "userType"

        @Ignore
        const val COLUMN_AGENCY_TYPE: String = "agencyType"

        @Ignore
        const val COLUMN_AGENCY_NAME: String = "agencyName"

        @Ignore
        const val COLUMN_WORK_CATEGORY: String = "work_category"

        @Ignore
        const val COLUMN_YEAR_IN_BUSINESS: String = "year_in_business"

        @Ignore
        const val COLUMN_BANK_NAME: String = "bankName"

        @Ignore
        const val COLUMN_NAME_ON_BANK: String = "nameOnBank"

        @Ignore
        const val COLUMN_ACCOUNT_NO: String = "accountNo"

        @Ignore
        const val COLUMN_EMERGENCY_MOBILE: String = "emergencymobile"

        @Ignore
        const val COLUMN_EMERGENCY_NAME: String = "emergencyname"

        @Ignore
        const val COLUMN_EMERGENCY_RELATION: String = "emergencyrelation"


        @Ignore
        const val COLUMN_REFERENCE_NAME: String = "referencename"

        @Ignore
        const val COLUMN_REFERENCE_MOBILE: String = "referencemobile"

        @Ignore
        const val COLUMN_CREATEDON: String = "createdon"

        @Ignore
        const val COLUMN_CREATEBY: String = "createdby"


        @Ignore
        const val COLUMN_REFERENCE_RELATION: String = "referencerelation"



        @Ignore
        const val COLUMN_IFSC: String = "ifsc"

        @Ignore
        const val COLUMN_BANKBRANCH: String = "bankbranch"

        @Ignore
        const val COLUMN_MOBILE_ON_BANK: String = "mobileOnBank"

        @Ignore
        const val COLUMN_STATE: String = "state"

        @Ignore
        const val COLUMN_DISTRICT_ID: String = "districtId"

        @Ignore
        const val COLUMN_STATE_ID: String = "stateId"

        @Ignore
        const val COLUMN_AADHAR_NO: String = "aadharNo"

        @Ignore
        const val COLUMN_PROFILE_PIC: String = "profilePic"

        @Ignore
        const val COLUMN_EMPLOYMENT: String = "employment"

        @Ignore
        const val COLUMN_MANAGERID: String = "managerid"

        @Ignore
        const val COLUMN_DEVICEID: String = "deviceid"

        @Ignore
        const val COLUMN_DOJ: String = "doj"

        @Ignore
        const val COLUMN_DOL: String = "dol"



        @Ignore
        const val COLUMN_PROFILE_PICTURE_UPDATED: String = "profile_pic_updated"
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

}