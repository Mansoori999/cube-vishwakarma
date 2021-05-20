package com.vinners.cube_vishwakarma.remote.retrofitServices

import com.vinners.cube_vishwakarma.data.models.Documents
import com.vinners.cube_vishwakarma.data.models.bank.Bank
import com.vinners.cube_vishwakarma.data.models.certificate.Certificate
import com.vinners.cube_vishwakarma.data.models.jobHistory.JobHistory
import com.vinners.cube_vishwakarma.data.models.profile.*
import com.vinners.cube_vishwakarma.data.models.refer.ReferResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileService {
    @GET("api/user/profile")
    suspend fun getProfile(): Response<List<UserProfile>>

    @Multipart
    @POST("/api/user/updatewithfile")
    suspend fun updloadBankDetails(
        @Query("type") bankKey: String?,
        @Part("data") bank: Bank,
        @Part imageFiles: MultipartBody.Part?,
        @Part imageFile_2: MultipartBody.Part?
    ): Response<List<String>>

    @GET("api/common/getdocuments")
    suspend fun getDocuments(): Response<List<Documents>>

    @GET("api/jobs/gethistory")
    suspend fun getJobHistory(@Query("pageno") pageNo: Int): Response<List<JobHistory>>

    @GET("api/application/mycertificates")
    suspend fun getCertificates(): Response<List<Certificate>>

    @GET("api/user/profiledetails")
    suspend fun getProfileDetails(): Response<List<ProfileDetails>>

    @GET("https://cutt.ly/api/api.php")
    suspend fun getShortReferralLink(
        @Query("key") key: String = "017dd6622c9610cc4bd8f0b7e8cf90ee14e7c",
        @Query("short") fullReferUrl: String
    ): Response<ReferResponse>

    @Multipart
    @POST("api/user/updateprofilepic")
    suspend fun updateProfilePic(@Part imageFile: MultipartBody.Part?): Response<List<UpdateProfilePic>>


    @GET("api/application/downloadcertficate")
    suspend fun downloadCertificate(@Query("applicationid")applicationid: String): Response<ResponseBody>

    @GET("api/user/allowedversion")
    suspend fun getAppVersion(@Query("version") version: String): Response<List<AppVersion>>


// changed password

    @POST("api/auth/changeuserpassword")
    suspend fun changedUserPassword(
            @Body passwordRequest:PasswordRequest
    ):Response<List<String>>

}