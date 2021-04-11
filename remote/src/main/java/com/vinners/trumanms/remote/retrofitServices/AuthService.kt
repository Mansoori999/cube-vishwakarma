package com.vinners.trumanms.remote.retrofitServices

import com.vinners.trumanms.data.models.auth.*
import com.vinners.trumanms.data.models.otp.OtpResponse
import com.vinners.trumanms.data.models.otp.VerifyOtpLoginRequest
import com.vinners.trumanms.data.models.otp.VerifyOtpRequest
import com.vinners.trumanms.data.models.password.ChangePasswordRequest
import com.vinners.trumanms.data.models.password.ForgotPasswordRequest
import com.vinners.trumanms.data.models.stateAndCity.*
import com.vinners.trumanms.remote.models.CheckUserRegistrationStatusRequest
import com.vinners.trumanms.remote.models.CheckUserRegistrationStatusResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("api/auth/verify")
    suspend fun checkUserRegistrationStatus(
        @Body request: CheckUserRegistrationStatusRequest
    ): Response<List<UserRegistrationCheckResponse>>


    @POST("api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<ArrayList<LoginResponse>>

    @POST("/api/auth/sendotp")
    suspend fun loginWithOtp(
        @Body loginWithOtpRequest: LoginWithOtpRequest
    ): Response<ArrayList<LoginWithOtpResponse>>

    @POST("api/auth/otpverify")
    suspend fun verifyOtp(
        @Body verifyOtpRequest: VerifyOtpRequest
    ): Response<ArrayList<LoginResponse>>

    @POST("api/auth/loginwithotp")
    suspend fun verifyOtpLogin(
        @Body verifyOtpRequest: VerifyOtpLoginRequest
    ): Response<ArrayList<LoginResponse>>

    @GET("api/auth/resendotp")
    suspend fun resendOtp(
        @Query("otpToken") otpToken: String
    ): Response<ArrayList<String>>

    @POST("api/auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<ArrayList<LoginResponse>>

    @GET("api/auth/forgotpassword/")
    suspend fun forgotPassword(
        @Query("mobile") mobile: String?
    ): Response<ArrayList<String>>

    @POST("api/auth/changepassword")
    suspend fun changePassword(
        @Body changePasswordRequest: ChangePasswordRequest
    ): Response<ArrayList<String>>

    @POST("/api/auth/register")
    suspend fun register()

    @GET("api/state/get")
    suspend fun getState(): Response<ArrayList<State>>

    @GET("api/city/bystate")
    suspend fun getCity(@Query("stateId") stateId: String?): Response<ArrayList<City>>

    @GET("api/pincode/bydistrict")
    suspend fun getPincode(@Query("districtId") districtId: String?): Response<ArrayList<Pincode>>

    @GET("api/common/citypincodebystateid")
    suspend fun getCityAndPincode(@Query("stateid") stateId: String?): Response<List<CityAndPincode>>

    @GET("api/jobs/getjobcategories")
    suspend fun getWorkCategory(): Response<List<WorkCategory>>
}