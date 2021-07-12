package com.vinners.cube_vishwakarma.remote.retrofitServices

import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.dashboard.ComplaintRequestWithStatus
import com.vinners.cube_vishwakarma.data.models.outlets.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface OutletService {

    @GET("api/outlet/getoutlet")
    suspend fun getOutlet(): Response<List<OutletsList>>

    @GET("api/outlet/getoutletdetailsbyid")
    suspend fun getOutletDetails(
        @Query("outletid") outletid:String):Response<List<OutletDetailsList>>


    @Multipart
    @POST("api/outlet/editoutlet")
    suspend fun editOutlet(
            @Part("data") editOutletRequest: EditOutletRequest,
            @Part images: List<MultipartBody.Part>
    ):Response<List<OutletResponse>>

    @POST("api/outlet/editoutletgps")
    suspend fun editOutletGps(
        @Body editOutletLocation: EditOutletLocation
    ):Response<List<OutletResponse>>


    @POST("api/complaint/getcomplaintbyoutletid")
    suspend fun getComplaintsByOutletid(
        @Body complaintRequest: ComplaintRequest
    ):Response<ArrayList<MyComplaintList>>

    @POST("api/complaint/getcomplaintwithstatus")
    suspend fun getComplaintWithStatus(
        @Body complaintRequestWithStatus: ComplaintRequestWithStatus
    ):Response<ArrayList<MyComplaintList>>
}