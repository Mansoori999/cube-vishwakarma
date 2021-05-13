package com.vinners.cube_vishwakarma.data.sessionManagement

import android.content.SharedPreferences
import com.vinners.cube_vishwakarma.data.models.LoggedInUser
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UserSessionManager @Inject constructor(
    @Named("session_dependent_pref") private val sharedPreferences: SharedPreferences
) {

    val sessionToken: String? get() = sharedPreferences.getString(SESSION_TOKEN, null)
    val userName: String? get() = sharedPreferences.getString(NAME,null)
    val userId: String? get() = sharedPreferences.getString(ID,null)
    val mobile: String? get() = sharedPreferences.getString(PHONE_NUMBER,null)
    val profilepic: String? get() = sharedPreferences.getString(PROFILE_PIC,null)
    val designation: String? get() = sharedPreferences.getString(DESIGNATION,null)
//    val empCode: String? get() = sharedPreferences.getString(EMP_CODE,null)

    /**
     * Logs Out
     */
     fun logOut() {
        removePreviousData()
    }

    suspend fun userLoggedIn(): Boolean {
        return true
    }

    suspend fun getLoggedInUser(): LoggedInUser? {
        val authToken = sessionToken ?: return null

        val mobileNumber = sharedPreferences.getString(PHONE_NUMBER,null) ?: throw IllegalStateException("UserSessionManager Illegal State : auth token is not null and mobile number stored is null")
        val name = sharedPreferences.getString(NAME,null)
        val email = sharedPreferences.getString(USER_EMAIL, null)
        val id = sharedPreferences.getString(ID,null)
        val profilepic= sharedPreferences.getString(PROFILE_PIC,null)
        val design = sharedPreferences.getString(DESIGNATION,null)

        return LoggedInUser(
            sessionToken = authToken,
            displayName = name,
            mobileNumber = mobileNumber,
            email = email,
            id = id,
            pic = profilepic,
            design = design
        )
    }

    suspend fun startNewSession(loggedInUserInfo: LoggedInUser) {
        removePreviousData()
        sharedPreferences.edit().apply {
            putString(SESSION_TOKEN, loggedInUserInfo.sessionToken)
            putString(PHONE_NUMBER,loggedInUserInfo.mobileNumber)
            putString(NAME,loggedInUserInfo.displayName)
            putString(USER_EMAIL,loggedInUserInfo.email)
            putString(ID,loggedInUserInfo.id)
            putString(profilepic,loggedInUserInfo.pic)
            putString(DESIGNATION,loggedInUserInfo.design)
        }.apply()
    }

//    fun addEmpCode(empCode: String?){
//        sharedPreferences.edit().apply {
//            putString(EMP_CODE,empCode)
//        }.apply()
//    }
    private fun removePreviousData(){
        sharedPreferences.edit().clear().apply()
    }

    companion object {

        private const val IS_USER_LOGGED_IN = "6oBPuPxpv5sJq4mobGQA"
        private const val SESSION_TOKEN = "ajZzG04i3GE8ze68KhWl"
        private const val EMP_CODE = "ajZzG04i3GE8ze68KhWlsjdnfjs"
        private const val NAME = "zTI6yF3Mmbz6fkFlhkrJ"
        private const val CITY_ID = "ofEHSWfATowOMeSU8mLV"
        private const val ID = "ofEHSWfATowOMeSU8mLVajsjas"
        private const val CITY_NAME = "ATowOofEHSU8mLVSWfMe"
        private const val PHONE_NUMBER = "p608Iy1pLX3prIHT2wnJ"
        private const val USER_EMAIL = "VYHsJ8ehzdAITLr9m0Bf"
        private const val PROFILE_PIC = "g5h5j6Q8jkjjuKp55RbjS"
        private const val DESIGNATION = "hUf9a4T3a8JSLPn44Id5e"
    }
}