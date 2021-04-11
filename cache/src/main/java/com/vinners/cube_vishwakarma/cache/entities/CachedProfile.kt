package com.vinners.cube_vishwakarma.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = CachedProfile.TABLE_NAME)
data class CachedProfile(

    @ColumnInfo(name = COLUMN_FIRST_NAME)
    val firstName: String? = null,

    @ColumnInfo(name = COLUMN_LAST_NAME)
    val lastName: String? = null,

    @ColumnInfo(name = COLUMN_MOBILE_NUMBER)
    val mobile: String? = null,

    @ColumnInfo(name = COLUMN_CITY_ID)
    val cityId: Int? = null,

    @ColumnInfo(name = COLUMN_CITY_NAME)
    val district: String? =  null,

    @ColumnInfo(name = COLUMN_LANGUAGE)
    val languages : String? = null,

    @ColumnInfo(name = COLUMN_TEAM_SIZE)
    val teamSize : String? = null,

    @ColumnInfo(name = COLUMN_EMAIL)
    var email: String? = null,

    @ColumnInfo(name = COLUMN_PIN_CODE)
    val pinCode: String? = null,

    @ColumnInfo(name = COLUMN_DATE_OF_BIRTH)
    val dob: String? = null,

    @ColumnInfo(name = COLUMN_ADDRESS)
    var fullAddress: String? = null,

    @ColumnInfo(name = COLUMN_EDUCATION)
    val education : String? = null,

    @ColumnInfo(name = COLUMN_DESIGNATION_NAME)
    var designationName: String? = null,

    @ColumnInfo(name = COLUMN_EXPERIENCE)
    val experience : String? = null,

    @ColumnInfo(name = COLUMN_WORK_CATEGORY)
    val workCategory : String? = null,

    @ColumnInfo(name = COLUMN_WEBSITE_PAGE)
    val websitePage : String? = null,

    @ColumnInfo(name = COLUMN_FACEBOOK_PAGE)
    val facebookPage : String? = null,

    @ColumnInfo(name = COLUMN_TWO_WHEELAR)
    val twoWheeler : Boolean,

    @ColumnInfo(name = COLUMN_WHATS_APP_MOBILE)
    val whatsAppMobileNumber: String? = null,

    @ColumnInfo(name = COLUMN_USER_TYPE)
    val userType: String? = null,

    @ColumnInfo(name = COLUMN_AGENCY_TYPE)
    val agencyType: String? = null,

    @ColumnInfo(name = COLUMN_GENDER)
    val gender: String? = null,

    @ColumnInfo(name = COLUMN_PINCODE_ID)
    val pinCodeId: Int? = null,

    @ColumnInfo(name = COLUMN_AGENCY_NAME)
    val agencyName: String? = null,

    @ColumnInfo(name = COLUMN_YEAR_IN_BUSINESS)
    val yearInBusiness: String? = null,

    @ColumnInfo(name = COLUMN_BANK_NAME)
    val bankName: String? = null,

    @ColumnInfo(name = COLUMN_NAME_ON_BANK)
    val nameOnBank: String? = null,

    @ColumnInfo(name = COLUMN_MOBILE_ON_BANK)
    val mobileOnBank: String? = null,

    @ColumnInfo(name = COLUMN_IFSC)
    val ifsc: String? = null,

    @ColumnInfo(name = COLUMN_ACCOUNT_NO)
    val accountNo: String? = null,

    @ColumnInfo(name = COLUMN_DISTRICT_ID)
    val districtId: Int? = null,

    @ColumnInfo(name = COLUMN_STATE_ID)
    val stateId: Int? = null,

    @ColumnInfo(name = COLUMN_STATE)
    val state: String? = null,

    @ColumnInfo(name = COLUMN_PROFILE_PIC)
    val profilePic: String? = null,

    @ColumnInfo(name = COLUMN_AADHAR_NO)
    val aadharNo: String? = null,

    @ColumnInfo(name = COLUMN_PROFILE_PICTURE_UPDATED)
    var profilePicUpdated: Boolean = false

) {

    companion object {
        @Ignore
        const val TABLE_NAME: String = "profile"

        @Ignore
        const val COLUMN_FIRST_NAME: String = "first_name"

        @Ignore
        const val COLUMN_LAST_NAME: String = "last_name"

        @Ignore
        const val COLUMN_MOBILE_NUMBER: String = "mobile_number"

        @Ignore
        const val COLUMN_GENDER: String = "gender"

        @Ignore
        const val COLUMN_CITY_ID: String = "city_id"

        @Ignore
        const val COLUMN_CITY_NAME: String = "city_name"


        @Ignore
        const val COLUMN_EMAIL: String = "email"

        @Ignore
        const val COLUMN_PIN_CODE: String = "pin_code"

        @Ignore
        const val COLUMN_DATE_OF_BIRTH: String = "dob"

        @Ignore
        const val COLUMN_ADDRESS: String = "address"

        @Ignore
        const val COLUMN_EDUCATION: String = "education"

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
        const val COLUMN_IFSC: String = "ifsc"

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
        const val COLUMN_PROFILE_PICTURE_UPDATED: String = "profile_pic_updated"
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

}