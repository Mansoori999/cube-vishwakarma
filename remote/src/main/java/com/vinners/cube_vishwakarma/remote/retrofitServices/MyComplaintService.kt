package com.vinners.cube_vishwakarma.remote.retrofitServices

import com.vinners.cube_vishwakarma.data.models.complaints.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MyComplaintService {

    @POST("api/complaint/getcomplaint")
    suspend fun getComplaint(
        @Body complaintUserIdRequet: ComplaintUserIdRequet
    ): Response<ArrayList<MyComplaintList>>

    @GET("api/complaint/getcomplaintdetails")
    suspend fun getComplaintDetails(
        @Query("id")id:String
    ):Response<List<MyComplainDetailsList>>

    @POST("api/complaint/updatecomplaint")
    suspend fun upDateComplaint(
         @Body upDateComplaintRequest: UpDateComplaintRequest
    ):Response<List<String>>

}