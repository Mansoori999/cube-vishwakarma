package com.vinners.cube_vishwakarma.remote.dataStoreImpl

import com.vinners.cube_vishwakarma.data.dataStores.AuthRemoteDataStore
import com.vinners.cube_vishwakarma.data.dataStores.userProfile.UserProfileLocalDataStore
import com.vinners.cube_vishwakarma.data.models.LoggedInUser
import com.vinners.cube_vishwakarma.data.models.auth.*
import com.vinners.cube_vishwakarma.data.models.jobs.JobDetails
import com.vinners.cube_vishwakarma.data.models.mdm.MobileInformation
import com.vinners.cube_vishwakarma.data.models.otp.VerifyOtpLoginRequest
import com.vinners.cube_vishwakarma.data.models.otp.VerifyOtpRequest
import com.vinners.cube_vishwakarma.data.models.password.ChangePasswordRequest
import com.vinners.cube_vishwakarma.data.models.password.ForgotPasswordRequest
import com.vinners.cube_vishwakarma.data.models.stateAndCity.*
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.models.CheckUserRegistrationStatusRequest
import com.vinners.cube_vishwakarma.remote.retrofitServices.AuthService
import javax.inject.Inject

class AuthRemoteDataStoreImpl @Inject constructor(
    private val authService: AuthService,
    private val userSessionManager: UserSessionManager
) : AuthRemoteDataStore {

    override suspend fun getUserRegistrationInfo(
        mobileNumber: String?,
        appHash: String?
    ): UserRegistrationCheckResponse {
        return authService.checkUserRegistrationStatus(
            CheckUserRegistrationStatusRequest(mobile = mobileNumber,appHash = appHash)
        ).bodyOrThrow().first()
    }

    override suspend fun login(
        mobileNumber: String?,
        password: String?,
        appHash : String?,
        mobileInformation: MobileInformation?
    ): LoginResponse {
        val response = authService.login(
            LoginRequest(
                userName = mobileNumber,
                password = password,
                appHash = appHash,
                mobileInfo = mobileInformation
            )
        ).bodyOrThrow().first()

        userSessionManager.startNewSession(LoggedInUser(
            id = response.id,
            sessionToken = response.authToken,
            displayName = response.firstName,
            mobileNumber = response.mobile,
            email = response.email,
            empCode = response.empcode
        ))
        return response
    }


    override suspend fun loginWithOtp(mobileNumber: String?, appHash: String?) : LoginWithOtpResponse {
       return authService.loginWithOtp(
            LoginWithOtpRequest(
                mobile = mobileNumber,
                appHash = appHash
            )
        ).bodyOrThrow().first()
    }

    override suspend fun verifyOtp(verifyOtpRequest: VerifyOtpRequest): LoginResponse {
     val response =   authService.verifyOtp(
           verifyOtpRequest
        ).bodyOrThrow().first()
        userSessionManager.startNewSession(LoggedInUser(
            id = response.id,
            sessionToken = response.authToken,
            displayName = response.firstName,
            mobileNumber = response.mobile,
            email = response.email,
            empCode = response.empcode
        ))
        return response
    }

    override suspend fun verifyOtpLogin(
    verifyOtpLoginRequest: VerifyOtpLoginRequest
    ): LoginResponse {
       val  response = authService.verifyOtpLogin(verifyOtpLoginRequest).bodyOrThrow().first()
        userSessionManager.startNewSession(LoggedInUser(
            id = response.id,
            sessionToken = response.authToken,
            displayName = response.firstName,
            mobileNumber = response.mobile,
            email = response.email,
            empCode = response.empcode
        ))
        return response
    }

    override suspend fun resendOtp(otpToken: String) {
        authService.resendOtp(otpToken).bodyOrThrow()
    }

    override suspend fun register(registerRequest: RegisterRequest) {
     val response = authService.register(registerRequest).bodyOrThrow().first()
        userSessionManager.addEmpCode(response.empcode)
    }

    override suspend fun forgotPassword(mobileNumber: String?) {
        authService.forgotPassword(
           mobileNumber
        ).bodyOrThrow()
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String) {
        authService.changePassword(
            ChangePasswordRequest(
                oldPassword = oldPassword,
                newPassword = newPassword
            )
        ).bodyOrThrow()
    }

    override suspend fun getState(): List<State> {
        return authService.getState().bodyOrThrow()
    }

    override suspend fun getCity(stateId: String?): List<City> {
        return authService.getCity(stateId).bodyOrThrow()
    }

    override suspend fun getPincode(districtId: String?): List<Pincode> {
        return authService.getPincode(districtId).bodyOrThrow()
    }

    override suspend fun getWorkCategory(): List<WorkCategory> {
        return authService.getWorkCategory().bodyOrThrow()
    }

    override suspend fun getCityAndPincode(stateId: String?): CityAndPincode {
        return authService.getCityAndPincode(stateId).bodyOrThrow().first()
    }
}