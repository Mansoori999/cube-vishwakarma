package com.vinners.cube_vishwakarma.remote.retrofitServices


import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.ComplaintOrderByList
import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.ComplaintOutletList
import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.ComplaintSubmitRequest
import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.ComplaintTypeList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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

}