package com.vinners.cube_vishwakarma.remote.retrofitServices

import com.vinners.cube_vishwakarma.data.models.complaints.*
import com.vinners.cube_vishwakarma.data.models.outlets.EditOutletRequest
import com.vinners.cube_vishwakarma.data.models.outlets.OutletResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface MyComplaintService {

    @POST("api/complaint/getcomplaint")
    suspend fun getComplaint(
        @Body complaintUserIdRequet: ComplaintUserIdRequet
    ): Response<ArrayList<MyComplaintList>>

    @GET("api/complaint/getcomplaintdetails")
    suspend fun getComplaintDetails(
        @Query("id")id:String
    ):Response<List<MyComplainDetailsList>>

    @Multipart
    @POST("api/complaint/updatecomplaint")
    suspend fun upDateComplaint(
            @Part("data") upDateComplaintRequest: UpDateComplaintRequest,
            @Part letterPic: MultipartBody.Part?,
            @Part measurementPic: MultipartBody.Part?,
            @Part layoutPic: MultipartBody.Part?
    ):Response<List<String>>

    @GET("api/user/getuserbymanagerid")
    suspend fun allocateUserForComplaint():Response<List<AllocateUserResponse>>

    @POST("api/complaint/allocateusertocomplaint")
    suspend fun requestAllocatedUserForComplaint(
            @Body allocateUserRequestForComplaint: AllocateUserRequestForComplaint
    ):Response<List<String>>

}