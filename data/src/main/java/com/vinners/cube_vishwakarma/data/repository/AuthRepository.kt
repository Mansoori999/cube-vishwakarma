package com.vinners.cube_vishwakarma.data.repository


import com.vinners.cube_vishwakarma.data.dataStores.AuthRemoteDataStore
import com.vinners.cube_vishwakarma.data.dataStores.userProfile.UserProfileLocalDataStore
import com.vinners.cube_vishwakarma.data.models.auth.*
import com.vinners.cube_vishwakarma.data.models.mdm.MobileInformation

import com.vinners.cube_vishwakarma.data.models.otp.VerifyOtpLoginRequest
import com.vinners.cube_vishwakarma.data.models.otp.VerifyOtpRequest
import com.vinners.cube_vishwakarma.data.models.profile.Profile
import com.vinners.cube_vishwakarma.data.models.stateAndCity.*
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
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
//    mobileInformation: MobileInformation?

    suspend fun login(
        mobileNumber: String?,
        password: String?,
        appHash: String?
    ): LoginWithOtpResponse {
        return authRemoteDataStore.login(mobileNumber, password, appHash)
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
                name = loginResponse.name,
                nickname = loginResponse.nickname,
                mobile = loginResponse.mobile,
                email = loginResponse.email,
                alternatemobile = loginResponse.alternatemobile,
                userType = loginResponse.userType,
                logintype = loginResponse.logintype,
                dob = loginResponse.dob,
                gender = loginResponse.gender,
                loginid = loginResponse.loginid,
                city = loginResponse.city,
                pincode = loginResponse.pincode,
                pic = loginResponse.pic,
                education = loginResponse.education,
                aadhaarno = loginResponse.aadhaarno,
                aadhaarpic = loginResponse.aadhaarpic,
                pan = loginResponse.pan,
                panpic = loginResponse.panpic,
                dlnumber = loginResponse.dlnumber,
                dlpic = loginResponse.dlpic,
                pfnumber = loginResponse.pfnumber,
                designation = loginResponse.designation,
                esicnumber = loginResponse.esicnumber,
                voterid = loginResponse.voterid,
                voteridpic = loginResponse.voteridpic,
                bankname = loginResponse.bankname,
                nameonbank = loginResponse.nameonbank,
                bankbranch = loginResponse.bankbranch,
                ifsc = loginResponse.ifsc,
                accountno = loginResponse.accountno,
                emergencymobile = loginResponse.emergencymobile,
                state = loginResponse.state,
                emergencyname = loginResponse.emergencyname,
                emergencyrelation = loginResponse.emergencyrelation,
                referencename = loginResponse.referencename,

                referencemobile = loginResponse.referencemobile,
                referencerelation = loginResponse.referencerelation,
                address = loginResponse.address,
                createdby = loginResponse.createdby,
                createdon = loginResponse.createdon,
                authToken = loginResponse.authToken,
                employment = loginResponse.employment,
                managerid = loginResponse.managerid,
                deviceid = loginResponse.deviceid,
                doj = loginResponse.doj,
                dol = loginResponse.dol,

            )
        )
        return loginResponse
    }

    suspend fun verifyOtpLogin(verifyOtpLoginRequest: VerifyOtpLoginRequest): LoginResponse {
        val loginResponse = authRemoteDataStore.verifyOtpLogin(verifyOtpLoginRequest)
        profileLocalDataStore.updateProfile(
            RegisterRequest(
                name = loginResponse.name,
                nickname = loginResponse.nickname,
                mobile = loginResponse.mobile,
                email = loginResponse.email,
                alternatemobile = loginResponse.alternatemobile,
                userType = loginResponse.userType,
                logintype = loginResponse.logintype,
                dob = loginResponse.dob,
                gender = loginResponse.gender,
                loginid = loginResponse.loginid,
                city = loginResponse.city,
                pincode = loginResponse.pincode,
                pic = loginResponse.pic,
                education = loginResponse.education,
                aadhaarno = loginResponse.aadhaarno,
                aadhaarpic = loginResponse.aadhaarpic,
                pan = loginResponse.pan,
                panpic = loginResponse.panpic,
                dlnumber = loginResponse.dlnumber,
                dlpic = loginResponse.dlpic,
                pfnumber = loginResponse.pfnumber,
                designation = loginResponse.designation,
                esicnumber = loginResponse.esicnumber,
                voterid = loginResponse.voterid,
                voteridpic = loginResponse.voteridpic,
                bankname = loginResponse.bankname,
                nameonbank = loginResponse.nameonbank,
                bankbranch = loginResponse.bankbranch,
                ifsc = loginResponse.ifsc,
                accountno = loginResponse.accountno,
                emergencymobile = loginResponse.emergencymobile,
                state = loginResponse.state,
                emergencyname = loginResponse.emergencyname,
                emergencyrelation = loginResponse.emergencyrelation,
                referencename = loginResponse.referencename,

                referencemobile = loginResponse.referencemobile,
                referencerelation = loginResponse.referencerelation,
                address = loginResponse.address,
                createdby = loginResponse.createdby,
                createdon = loginResponse.createdon,
                authToken = loginResponse.authToken,
                employment = loginResponse.employment,
                managerid = loginResponse.managerid,
                deviceid = loginResponse.deviceid,
                doj = loginResponse.doj,
                dol = loginResponse.dol,
            )
        )
        return loginResponse
    }

    suspend fun resendOtp(
        otpToken: String
    ) {
            authRemoteDataStore.resendOtp(otpToken)
    }

//    suspend fun register(registerRequest: RegisterRequest) {
//        authRemoteDataStore.register(registerRequest)
//        profileLocalDataStore.updateProfile(registerRequest)
//    }

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