package com.vinners.cube_vishwakarma.remote.retrofitServices


import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ComplaintRequestService {

    @GET("api/master/getallactiveoutlet")
    suspend fun getComplaintList(): Response<List<ComplaintOutletList>>

    @GET("api/master/getcomplainttype")
    suspend fun getComplaintTypeList(): Response<List<ComplaintTypeList>>

    @GET("api/user/getuserbytype?usertype=client")
    suspend fun getOrderByList() : Response<List<ComplaintOrderByList>>

    @POST("api/complaint/requestcomplaint")
    suspend fun submitComplaintRequestData(
            @Body complaintSubmitRequest: ComplaintSubmitRequest
    ) : Response<List<String>>

    @POST("api/complaint/getmycomplaintrequest")
    suspend fun getComplaintRequestViewActivityList(
        @Body complaintRequestViewRequest: ComplaintRequestViewRequest
    ): Response<List<ComplaintRequestResponse>>

}