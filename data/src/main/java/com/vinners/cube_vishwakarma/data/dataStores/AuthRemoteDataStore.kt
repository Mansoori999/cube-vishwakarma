package com.vinners.cube_vishwakarma.data.dataStores

import com.vinners.cube_vishwakarma.data.models.auth.*
import com.vinners.cube_vishwakarma.data.models.jobs.JobDetails
import com.vinners.cube_vishwakarma.data.models.mdm.MobileInformation
import com.vinners.cube_vishwakarma.data.models.otp.VerifyOtpLoginRequest
import com.vinners.cube_vishwakarma.data.models.otp.VerifyOtpRequest
import com.vinners.cube_vishwakarma.data.models.stateAndCity.*

interface AuthRemoteDataStore {

    suspend fun getUserRegistrationInfo(
        mobileNumber: String?,
        appHash: String?
    ): UserRegistrationCheckResponse
//    mobileInformation: MobileInformation?
//    suspend fun login(
//        mobileNumber: String?,
//        password: String?,
//        appHash: String?
//
//    ): LoginResponse

    suspend fun login(
            mobileNumber: String?,
            password: String?,
            appHash: String?

    ): LoginWithOtpResponse

    suspend fun loginWithOtp(
        mobileNumber: String?,
        appHash: String?
    ): LoginWithOtpResponse


    suspend fun verifyOtp(
       verifyOtpRequest: VerifyOtpRequest
    ): LoginResponse

    suspend fun verifyOtpLogin(verifyOtpLoginRequest: VerifyOtpLoginRequest):LoginResponse

    suspend fun resendOtp(
        otpToken: String
    )

//    suspend fun register(registerRequest: RegisterRequest)

    suspend fun forgotPassword(
        mobileNumber: String?
    )

    suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    )

    suspend fun getState(): List<State>

    suspend fun getCity(stateId: String?): List<City>

    suspend fun getPincode(districtId: String?): List<Pincode>

    suspend fun getWorkCategory(): List<WorkCategory>

    suspend fun getCityAndPincode(stateId: String?): CityAndPincode

    // login without otp
    suspend fun loginWithoutOtp(loginWithoutOtpRequest: LoginWithoutOtpRequest):LoginResponse

}