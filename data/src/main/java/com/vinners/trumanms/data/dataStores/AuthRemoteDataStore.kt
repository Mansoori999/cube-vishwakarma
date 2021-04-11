package com.vinners.trumanms.data.dataStores

import com.vinners.trumanms.data.models.auth.*
import com.vinners.trumanms.data.models.jobs.JobDetails
import com.vinners.trumanms.data.models.mdm.MobileInformation
import com.vinners.trumanms.data.models.otp.VerifyOtpLoginRequest
import com.vinners.trumanms.data.models.otp.VerifyOtpRequest
import com.vinners.trumanms.data.models.stateAndCity.*

interface AuthRemoteDataStore {

    suspend fun getUserRegistrationInfo(
        mobileNumber: String?,
        appHash: String?
    ): UserRegistrationCheckResponse

    suspend fun login(
        mobileNumber: String?,
        password: String?,
        appHash: String?,
        mobileInformation: MobileInformation?
    ): LoginResponse

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

    suspend fun register(registerRequest: RegisterRequest)

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


}