package com.vinners.trumanms.data.repository


import com.vinners.trumanms.data.dataStores.AuthRemoteDataStore
import com.vinners.trumanms.data.dataStores.userProfile.UserProfileLocalDataStore
import com.vinners.trumanms.data.models.auth.*
import com.vinners.trumanms.data.models.mdm.MobileInformation
import com.vinners.trumanms.data.models.otp.VerifyOtpLoginRequest
import com.vinners.trumanms.data.models.otp.VerifyOtpRequest
import com.vinners.trumanms.data.models.profile.Profile
import com.vinners.trumanms.data.models.stateAndCity.*
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authRemoteDataStore: AuthRemoteDataStore,
    private val userSessionManager: UserSessionManager,
    private val profileLocalDataStore: UserProfileLocalDataStore
) {
    fun observeProfile(): Observable<Profile> {
        return profileLocalDataStore.getProfile()
    }

    suspend fun getUserRegistrationInfo(
        mobileNumber: String?,
        appHash: String?
    ): UserRegistrationCheckResponse {
        return authRemoteDataStore.getUserRegistrationInfo(
            mobileNumber,
            appHash
        )
    }

    suspend fun login(
        mobileNumber: String?,
        password: String?,
        appHash: String?,
        mobileInformation: MobileInformation?
    ): LoginResponse {
        return authRemoteDataStore.login(mobileNumber, password, appHash, mobileInformation)
    }

    suspend fun loginWithOtp(
        mobileNumber: String?,
        appHash: String?
    ): LoginWithOtpResponse {
        return authRemoteDataStore.loginWithOtp(mobileNumber, appHash)
    }

    suspend fun verifyOtp(verifyOtpRequest: VerifyOtpRequest): LoginResponse {
        val loginResponse = authRemoteDataStore.verifyOtp(verifyOtpRequest)
        profileLocalDataStore.updateProfile(
            RegisterRequest(
                firstName = loginResponse.firstName,
                lastName = loginResponse.lastName,
                mobile = loginResponse.mobile,
                email = loginResponse.email,
                agencyType = loginResponse.agencyType,
                userType = loginResponse.userType,
                whatsAppMobileNumber = loginResponse.whatsAppMobileNumber,
                dob = loginResponse.dob,
                gender = loginResponse.gender,
                district = loginResponse.district,
                cityId = loginResponse.cityId,
                pinCode = loginResponse.pincode,
                pinCodeId = loginResponse.pinCodeId,
                education = loginResponse.education,
                experience = loginResponse.experience,
                languages = loginResponse.languages,
                twoWheeler = loginResponse.twoWheeler,
                workCategory = loginResponse.workCategory,
                teamSize = loginResponse.teamSize,
                websitePage = loginResponse.websitePage,
                facebookPage = loginResponse.facebookPage,
                designation = loginResponse.designation,
                password = "pass",
                agencyName = loginResponse.agencyName,
                yearsInBusiness = loginResponse.yearsinbusiness,
                bankName = loginResponse.bankName,
                nameOnBank = loginResponse.nameOnBank,
                mobileOnBank = loginResponse.mobileOnBank,
                ifsc = loginResponse.ifsc,
                accountNo = loginResponse.accountNo,
                stateId = loginResponse.stateId,
                state = loginResponse.state,
                districtId = loginResponse.districtId,
                profilePic = loginResponse.profilePic,
                adhar = loginResponse.adhar
            )
        )
        return loginResponse
    }

    suspend fun verifyOtpLogin(verifyOtpLoginRequest: VerifyOtpLoginRequest): LoginResponse {
        val loginResponse = authRemoteDataStore.verifyOtpLogin(verifyOtpLoginRequest)
        profileLocalDataStore.updateProfile(
            RegisterRequest(
                firstName = loginResponse.firstName,
                lastName = loginResponse.lastName,
                mobile = loginResponse.mobile,
                email = loginResponse.email,
                agencyType = loginResponse.agencyType,
                userType = loginResponse.userType,
                whatsAppMobileNumber = loginResponse.whatsAppMobileNumber,
                dob = loginResponse.dob,
                gender = loginResponse.gender,
                district = loginResponse.district,
                cityId = loginResponse.cityId,
                pinCode = loginResponse.pincode,
                pinCodeId = loginResponse.pinCodeId,
                education = loginResponse.education,
                experience = loginResponse.experience,
                languages = loginResponse.languages,
                twoWheeler = loginResponse.twoWheeler,
                workCategory = loginResponse.workCategory,
                teamSize = loginResponse.teamSize,
                websitePage = loginResponse.websitePage,
                facebookPage = loginResponse.facebookPage,
                designation = loginResponse.designation,
                password = "pass",
                agencyName = loginResponse.agencyName,
                yearsInBusiness = loginResponse.yearsinbusiness,
                bankName = loginResponse.bankName,
                nameOnBank = loginResponse.nameOnBank,
                mobileOnBank = loginResponse.mobileOnBank,
                ifsc = loginResponse.ifsc,
                accountNo = loginResponse.accountNo,
                stateId = loginResponse.stateId,
                state = loginResponse.state,
                districtId = loginResponse.districtId,
                profilePic = loginResponse.profilePic,
                adhar =  loginResponse.adhar
            )
        )
        return loginResponse
    }

    suspend fun resendOtp(
        otpToken: String
    ) {
            authRemoteDataStore.resendOtp(otpToken)
    }

    suspend fun register(registerRequest: RegisterRequest) {
        authRemoteDataStore.register(registerRequest)
        profileLocalDataStore.updateProfile(registerRequest)
    }

    suspend fun forgotPassword(
        mobileNumber: String?
    ) {
        authRemoteDataStore.forgotPassword(mobileNumber)
    }

    suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ) {

    }

    suspend fun getState(): List<State> {
        return authRemoteDataStore.getState()
    }

    suspend fun getCity(stateId: String?): List<City> {
        return authRemoteDataStore.getCity(stateId)
    }

    suspend fun getPincode(districtId: String?): List<Pincode> {
        return authRemoteDataStore.getPincode(districtId)
    }

    suspend fun getWorkCategory(): List<WorkCategory> {
        return authRemoteDataStore.getWorkCategory()
    }

    suspend fun getCityAndPincode(stateId: String?): CityAndPincode{
        return authRemoteDataStore.getCityAndPincode(stateId)
    }
}